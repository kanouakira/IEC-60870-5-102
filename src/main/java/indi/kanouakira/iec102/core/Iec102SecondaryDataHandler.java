package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum;
import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;
import indi.kanouakira.iec102.core.enums.TypeIdentificationEnum;
import indi.kanouakira.iec102.standard.ChannelHandler;
import indi.kanouakira.iec102.standard.DataConfig;
import indi.kanouakira.iec102.standard.DataHandler;
import indi.kanouakira.iec102.standard.MessageDetail;

import java.util.List;
import java.util.stream.Collectors;

import static indi.kanouakira.iec102.core.Iec102DataConfig.getWaitingForUpload;
import static indi.kanouakira.iec102.core.Iec102FixedMessageDetail.createFixedMessageDetail;
import static indi.kanouakira.iec102.core.Iec102VariableMessageDetail.crateVariableMessageDetail;
import static indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum.TRANSMISSION_CONTINUE;
import static indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum.TRANSMISSION_FINISHED;
import static indi.kanouakira.iec102.core.enums.FunctionCodeEnum.*;
import static indi.kanouakira.iec102.util.ByteUtil.byteArrayToHexString;

/**
 * @author KanouAkira
 * @date 2022/4/22 18:08
 */
public class Iec102SecondaryDataHandler extends DataHandler {

    private final int PRM = 0;

    private final int BATCH_SIZE = 300;

    private int FCB = 0;

    /* ACD位为1表示有一级数据待上传，主站收到后会召唤一级数据。 */
    private int ACD = 0;

    /* 记录上一次发送的报文 */
    private MessageDetail lastResponse;

    /* 记录上一次发送的文件名 */
    private Iec102UploadFile lastSendFile;

    private List<Iec102UploadFile> uploadFileList;

    protected Iec102SecondaryDataHandler(DataConfig dataConfig) {
        super(dataConfig);
    }

    @Override
    public void handlerAdded(ChannelHandler channelHandler) {
        System.out.println("子站建立连接");
    }

    @Override
    public void channelReceived(ChannelHandler channelHandler, MessageDetail messageDetail) {
        refreshFileList();
        System.out.println(String.format("收到%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(messageDetail.encode())));
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
            channelHandler.writeAndFlush(lastResponse);
            System.out.println(String.format("重发%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(lastResponse.encode())));
            return;
        }else {
            FCB = messageDetailFcb;
        }

        switch (functionCodeEnum){
            case RESET_COMMUNICATE_UNIT -> {
                // 收到复位通信单元处理，FCB 置 0，发送确认帧。
                FCB = 0;
                lastResponse = createFixedMessageDetail(PRM, ACD, RESET_COMMUNICATE_UNIT_CONFIRM);
                break;
            }
            case SUMMON_LINK_STATUS -> {
                // 收到召唤链路状态处理
                lastResponse = createFixedMessageDetail(PRM, ACD, RESET_COMMUNICATE_UNIT_CONFIRM);
                break;
            }
            case SUMMON_CLASS_TWO -> {
                // 无论如何都以无可用数据响应召唤二级数据。
                // 但是有需要上报的数据时，ACD为1。主站接收到后会以召唤一级数据来获取。
                lastResponse = createFixedMessageDetail(PRM, ACD, NO_DATA_AVAILABLE);
                break;
            }
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

                TypeIdentificationEnum typeIdentificationEnum = lastSendFile.getTypeIdentificationEnum();
                data = lastSendFile.readDataBytes(BATCH_SIZE);
                CauseOfTransmissionEnum causeOfTransmissionEnum;
                if (data.length < BATCH_SIZE){
                    causeOfTransmissionEnum = TRANSMISSION_FINISHED;
                }else {
                    causeOfTransmissionEnum = TRANSMISSION_CONTINUE;
                }
                lastResponse = crateVariableMessageDetail(PRM, ACD, data, DATA_RESPONSE, typeIdentificationEnum, causeOfTransmissionEnum);
                break;
            }
        }
        channelHandler.writeAndFlush(lastResponse);
        System.out.println(String.format("回复%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(lastResponse.encode())));
    }

    @Override
    public void handlerRemoved(ChannelHandler channelHandler) {
        System.out.println("子站连接断开");
    }

    private void refreshFileList(){
        List<Iec102UploadFile> waitingForUpload = getWaitingForUpload();
        long currentTimeMillis = System.currentTimeMillis();
        uploadFileList = waitingForUpload.stream()
                .filter(file -> file.isNotExpired(currentTimeMillis)) // 未过期
                .filter(file -> file.isNotUploaded()) // 未上传
                .collect(Collectors.toList());
        ACD = uploadFileList.size() > 0 ? 1 : 0;

        System.out.println(String.format("检查，ACD：%d， 待上传文件数：%d", ACD, uploadFileList.size()));
    }

}
