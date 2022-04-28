package indi.kanouakira.iec102.standard;

/**
 * 数据处理方式抽象。
 *
 * @author KanouAkira
 * @date 2022/4/18 16:46
 */
public abstract class DataHandler {

    protected DataConfig dataConfig;

    protected DataHandler(DataConfig dataConfig) {
        this.dataConfig = dataConfig;
    }

    /**
     * @param channelHandler 处理器
     * @Title: handlerAdded
     * @Description: 建立连接时处理操作
     */
    abstract public void handlerAdded(ChannelHandler channelHandler);

    /**
     * @param channelHandler 处理器
     * @param messageDetail  传输帧即信息
     * @Title: channelReceived
     * @Description: 收到信息时处理操作
     */
    abstract public void channelReceived(ChannelHandler channelHandler, MessageDetail messageDetail);


    /**
     * @param channelHandler 处理器
     * @Title: handlerAdded
     * @Description: 连接断开时处理操作
     */
    abstract public void handlerRemoved(ChannelHandler channelHandler);

    public void handlerActive(ChannelHandler channelHandler){}

    public void handlerInactive(ChannelHandler channelHandler){}

}
