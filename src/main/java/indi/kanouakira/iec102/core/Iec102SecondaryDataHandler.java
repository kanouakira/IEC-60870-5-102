package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum;
import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;
import indi.kanouakira.iec102.core.enums.TypeIdentificationEnum;
import indi.kanouakira.iec102.standard.ChannelHandler;
import indi.kanouakira.iec102.standard.DataConfig;
import indi.kanouakira.iec102.standard.DataHandler;
import indi.kanouakira.iec102.standard.MessageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import static indi.kanouakira.iec102.core.Iec102DataConfig.getWaitingForUpload;
import static indi.kanouakira.iec102.core.Iec102FixedMessageDetail.createFixedMessageDetail;
import static indi.kanouakira.iec102.core.Iec102VariableMessageDetail.creatVariableMessageDetail;
import static indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum.*;
import static indi.kanouakira.iec102.core.enums.FunctionCodeEnum.*;
import static indi.kanouakira.iec102.util.ByteUtil.*;
import static indi.kanouakira.iec102.util.ContextUtil.getBean;

/**
 * IEC 60870-5-102 从站数据处理，改造以实现文件上送。
 *
 * @author KanouAkira
 * @date 2022/4/22 18:08
 */
public class Iec102SecondaryDataHandler extends DataHandler {

    final static Logger logger = LoggerFactory.getLogger(Iec102SecondaryDataHandler.class);

    /* 表示从站 */
    private final int PRM = 0;

    /* 文件分批发送大小，300字节。 */
    private final int BATCH_SIZE = 300;

    /* 记录主站的 FCB 位，初始0。 */
    private int FCB = 0;

    /* ACD位为1表示有一级数据待上传，主站收到后会召唤一级数据。 */
    private int ACD = 0;

    /* 记录上一次发送的报文 */
    private MessageDetail lastResponse;

    /* 记录上一次发送的文件 */
    private Iec102UploadFile lastSendFile;

    /* 待上送文件列表 */
    private List<Iec102UploadFile> uploadFileList;

    protected Iec102SecondaryDataHandler(DataConfig dataConfig) {
        super(dataConfig);
    }

    @Override
    public void handlerAdded(ChannelHandler channelHandler) {
        logger.debug("子站建立连接");
    }

    @Override
    public void channelReceived(ChannelHandler channelHandler, MessageDetail messageDetail) {
        logger.debug(String.format("收到%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(messageDetail.encode())));
        refreshFileList();
        Iec102MessageDetail iec102MessageDetail;
        if (messageDetail instanceof Iec102FixedMessageDetail){
            iec102MessageDetail = (Iec102MessageDetail)messageDetail;
        }else {
            throw new RuntimeException(String.format("非法报文:%s", byteArrayToHexString(messageDetail.encode())));
        }
        // FCB 位没变且功能码不为复位通信单元，发送上次报文。
        // 如果变了记录新的 FCB 位
        FunctionCodeEnum functionCodeEnum = iec102MessageDetail.getFunctionCodeEnum();
        int messageDetailFcb = iec102MessageDetail.getFcb();
        if (FCB == messageDetailFcb && !RESET_COMMUNICATE_UNIT.equals(functionCodeEnum)) {
            if (lastResponse != null) {
                channelHandler.writeAndFlush(lastResponse);
                logger.debug(String.format("重发%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(lastResponse.encode())));
            }
            return;
        }else {
            FCB = messageDetailFcb;
        }

        // 根据功能码进行处理。
        switch (functionCodeEnum){
            // 收到复位通信单元处理，FCB 置 0，发送确认帧。
            case RESET_COMMUNICATE_UNIT -> {
                FCB = 0;
                lastResponse = createFixedMessageDetail(PRM, ACD, RESET_COMMUNICATE_UNIT_CONFIRM);
            }
            // 收到召唤链路状态处理。
            case SUMMON_LINK_STATUS -> lastResponse = createFixedMessageDetail(PRM, ACD, RESET_COMMUNICATE_UNIT_CONFIRM);
            // 无论如何都以无可用数据响应召唤二级数据。
            // 但是有需要上报的数据时，ACD为1。主站接收到后会以召唤一级数据来获取。
            case SUMMON_CLASS_TWO -> lastResponse = createFixedMessageDetail(PRM, ACD, NO_DATA_AVAILABLE);
            // 收到召唤一级数据即上报数据。
            case SUMMON_CLASS_ONE -> {
                byte[] data;
                if (lastSendFile == null){
                    // 无数据时以无可用数据响应。
                    if (ACD == 0) {
                        lastResponse = createFixedMessageDetail(PRM, ACD, NO_DATA_AVAILABLE);
                        break;
                    }else {
                        lastSendFile = this.uploadFileList.get(0);
                    }
                }

                // 根据剩余待上送内容是否大于批大小设置传输原因。
                TypeIdentificationEnum typeIdentificationEnum = lastSendFile.getTypeIdentificationEnum();
                data = lastSendFile.readDataBytes(BATCH_SIZE);
                if (data == null)
                    throw new RuntimeException("待上送文件已无需读取内容");
                CauseOfTransmissionEnum causeOfTransmissionEnum;
                if (data.length < BATCH_SIZE){
                    causeOfTransmissionEnum = TRANSMISSION_FINISHED;
                    // 单文件传输结束时重新校验 ACD 确认是否有其他文件待上传。
                    if (this.uploadFileList.size() <= 1) {
                        ACD = 0;
                    }
                    lastSendFile.setUploadComplete();
                }else {
                    causeOfTransmissionEnum = TRANSMISSION_CONTINUE;
                }
                lastResponse = creatVariableMessageDetail(PRM, ACD, data, DATA_RESPONSE, typeIdentificationEnum, causeOfTransmissionEnum);
            }
            // 收到传送数据。
            case TRANSFORM_DATA ->{
                Iec102VariableMessageDetail detail;
                if (iec102MessageDetail instanceof Iec102VariableMessageDetail){
                    detail = (Iec102VariableMessageDetail) iec102MessageDetail;
                }else {
                    throw new RuntimeException("错误的传输数据帧格式。");
                }
                CauseOfTransmissionEnum cause = detail.getCause();
                switch (cause){
                    // 传输结束处理
                    case ACTIVATION_TERMINATION -> {
                        // 主站表示收到的文件长度
                        int receiveLength = byteArrayToInt(reverse(detail.getData()));

                        CauseOfTransmissionEnum causeOfTransmissionEnum;
                        // 主站表示收到的文件长度与实际发送的长度不符
                        if (receiveLength != lastSendFile.getDataLength()){
                            causeOfTransmissionEnum = RESEND_CONFIRM;
                            lastSendFile.resetReadIndex();
                            lastSendFile.resetUploaded();
                            ACD = 1;
                        }else {
                            causeOfTransmissionEnum = SEND_CONFIRM;
                            // 结果回调
                            getBean(Iec102CallbackHandler.class).handleResult(lastSendFile);
                            lastSendFile = null;
                            ACD = this.uploadFileList.size() > 1 ? 1 : 0;
                        }
                        lastResponse = creatVariableMessageDetail(PRM, ACD, detail.getData(), TRANSFORM_DATA_RESPONSE, detail.getType(), causeOfTransmissionEnum);
                    }
                    default -> throw new RuntimeException(String.format("传输数据时收到未处理的传输原因：%s，code=%d", cause.name(), cause.getValue()));
                }
            }
        }
        channelHandler.writeAndFlush(lastResponse);
        logger.debug(String.format("回复%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(lastResponse.encode())));
    }

    @Override
    public void handlerRemoved(ChannelHandler channelHandler) {
        logger.debug("子站连接断开");
    }

    /**
     * 重新检查是否存在待上传文件，同时更新 ACD 值。
     */
    private void refreshFileList(){
        List<Iec102UploadFile> waitingForUpload = getWaitingForUpload();
        long currentTimeMillis = System.currentTimeMillis();
        uploadFileList = waitingForUpload.stream()
                .filter(file -> file.isNotExpired(currentTimeMillis)) // 未过期
                .filter(file -> file.isNotUploaded()) // 未上传
                .collect(Collectors.toList());
        ACD = uploadFileList.size() > 0 ? 1 : 0;
        logger.debug(String.format("检查，ACD：%d， 待上传文件数：%d", ACD, uploadFileList.size()));
    }

}
