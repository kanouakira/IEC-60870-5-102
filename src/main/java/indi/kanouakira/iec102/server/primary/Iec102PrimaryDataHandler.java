package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.core.Iec102FixedMessageDetail;
import indi.kanouakira.iec102.core.Iec102MessageDetail;
import indi.kanouakira.iec102.core.Iec102VariableMessageDetail;
import indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum;
import indi.kanouakira.iec102.core.enums.FunctionCodeEnum;
import indi.kanouakira.iec102.standard.ChannelHandler;
import indi.kanouakira.iec102.standard.DataConfig;
import indi.kanouakira.iec102.standard.DataHandler;
import indi.kanouakira.iec102.standard.MessageDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import static indi.kanouakira.iec102.core.Iec102Constant.FILENAME_BYTE_LENGTH;
import static indi.kanouakira.iec102.core.enums.CauseOfTransmissionEnum.TRANSMISSION_FINISHED;
import static indi.kanouakira.iec102.core.enums.FunctionCodeEnum.TRANSFORM_DATA;
import static indi.kanouakira.iec102.core.enums.TypeIdentificationEnum.TRANSFORM_END;
import static indi.kanouakira.iec102.util.ByteUtil.*;
import static java.lang.System.arraycopy;

/**
 * @author KanouAkira
 * @date 2022/5/23 11:23
 */
public class Iec102PrimaryDataHandler extends DataHandler {

    final static Logger logger = LoggerFactory.getLogger(Iec102PrimaryDataHandler.class);

    /* 表示主站 */
    private final int PRM = 1;

    /* 记录主站的 FCB 位，初始0。 */
    private int FCB = 0;

    /* 记录接收文件的内容 */
    private byte[] fileContent = new byte[0];

    /* 记录接收文件的名字 */
    private String fileName;

    protected Iec102PrimaryDataHandler(DataConfig dataConfig) {
        super(dataConfig);
    }

    @Override
    public void handlerAdded(ChannelHandler channelHandler) {
    }

    @Override
    public void handlerActive(ChannelHandler channelHandler) {
        logger.debug("主站：与{}建立连接", channelHandler.getChannel().remoteAddress());
        // 启动后发送请求链路状态
        channelHandler.writeAndFlush(Iec102FixedMessageDetail.summonLinkStatus(FCB));
    }

    @Override
    public void channelReceived(ChannelHandler channelHandler, MessageDetail messageDetail) {
        logger.debug(String.format("收到%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(messageDetail.encode())));

        Iec102MessageDetail iec102MessageDetail;
        if (messageDetail instanceof Iec102FixedMessageDetail) {
            iec102MessageDetail = (Iec102MessageDetail) messageDetail;
        } else {
            throw new RuntimeException(String.format("非法报文:%s", byteArrayToHexString(messageDetail.encode())));
        }
        // 收到合法报文，FCB 位取反
        FCB ^= 0b00000001;

        FunctionCodeEnum functionCodeEnum = iec102MessageDetail.getFunctionCodeEnum();
        MessageDetail message = null;
        // 根据功能码进行处理。
        switch (functionCodeEnum) {
            // 收到召唤链路状态
            case SUMMON_LINK_STATUS_CONFIRM: {
                // 确认链路状态后发送复位通讯单元
                message = Iec102FixedMessageDetail.resetCommunicateUnit(FCB);
                break;
            }
            case RESET_COMMUNICATE_UNIT_CONFIRM: {
                FCB = 1;
                message = Iec102FixedMessageDetail.summonClassTwo(FCB);
                break;
            }
            case NO_DATA_AVAILABLE: {
                int acd = iec102MessageDetail.getFcbOrAcd();
                if (acd == 1) {
                    message = Iec102FixedMessageDetail.summonClassOne(FCB);
                } else {
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    message = Iec102FixedMessageDetail.summonClassTwo(FCB);
                }
                break;
            }
            // 接收到数据
            case DATA_RESPONSE: {
                Iec102VariableMessageDetail variableMessageDetail = (Iec102VariableMessageDetail) iec102MessageDetail;
                CauseOfTransmissionEnum cause = variableMessageDetail.getCause();
                switch (cause) {
                    case TRANSMISSION_CONTINUE: {
                        handleData(variableMessageDetail.getData());
                        // 继续召唤一类数据
                        message = Iec102FixedMessageDetail.summonClassOne(FCB);
                        break;
                    }
                    case TRANSMISSION_FINISHED: {
                        handleData(variableMessageDetail.getData());
                        // 确认传输完成计算接收到的文件长度后发送传输结束确认
                        byte[] data = reverse(intToByteArray(fileContent.length));
                        message = Iec102VariableMessageDetail.creatVariableMessageDetail(PRM, FCB, data, TRANSFORM_DATA, TRANSFORM_END.getValue(), TRANSMISSION_FINISHED);
                        break;
                    }
                    default: {
                        throw new RuntimeException(String.format("传输数据时收到未处理的传输原因：%s，code=%d", cause.name(), cause.getValue()));
                    }
                }
                break;
            }
            // 收到传送数据结束确认响应
            case TRANSFORM_DATA_RESPONSE: {
                Iec102VariableMessageDetail variableMessageDetail = (Iec102VariableMessageDetail) iec102MessageDetail;
                CauseOfTransmissionEnum cause = variableMessageDetail.getCause();
                switch (cause) {
                    case SEND_CONFIRM: {
                        // 确认传输正常，处理文件
                        logger.info("文件{}传输成功，文件长度{}", fileName, fileContent.length);
                        logger.debug("接收文件内容:{}", byteArrayToHexString(fileContent));
                        message = Iec102FixedMessageDetail.summonClassTwo(FCB);
                        break;
                    }
                    case RESEND_CONFIRM: {
                        logger.warn("文件{}传输失败，准备重新传输", fileName);
                        message = Iec102FixedMessageDetail.summonClassOne(FCB);
                        break;
                    }
                    default: {
                        throw new RuntimeException(String.format("传送数据结束确认时收到未处理的传输原因：%s，code=%d", cause.name(), cause.getValue()));
                    }
                }
                // 重置接收内容。
                fileName = null;
                fileContent = new byte[0];
                break;
            }
        }
        if (message != null) {
            channelHandler.writeAndFlush(message);
            logger.debug(String.format("回复%s报文: %s", channelHandler.getChannel().remoteAddress(), byteArrayToHexString(message.encode())));
        }
    }

    @Override
    public void handlerRemoved(ChannelHandler channelHandler) {
        logger.debug("主站：与{}断开连接", channelHandler.getChannel().remoteAddress());
    }

    /**
     * 处理接收到的用户数据部分
     *
     * @param data 报文中的用户数据字节数组
     */
    private void handleData(byte[] data) {
        int receiveLength = data.length - FILENAME_BYTE_LENGTH;
        try {
            byte[] bytes = Arrays.copyOf(data, FILENAME_BYTE_LENGTH);
            fileName = new String(bytes, "UTF-8").trim();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        // 处理保存接受到的字节数组
        logger.debug("收到文件：{}", fileName);
        byte[] newFileContent = new byte[fileContent.length + receiveLength];
        arraycopy(fileContent, 0, newFileContent, 0, fileContent.length);
        arraycopy(Arrays.copyOfRange(data, FILENAME_BYTE_LENGTH, data.length), 0, newFileContent, fileContent.length, receiveLength);
        fileContent = newFileContent;
    }

}
