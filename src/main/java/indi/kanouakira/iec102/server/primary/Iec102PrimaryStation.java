package indi.kanouakira.iec102.server.primary;

/**
 * @author KanouAkira
 * @date 2022/5/23 11:25
 */
public class Iec102PrimaryStation extends PrimaryStation {

    /**
     * @param host 连接主机地址
     * @param port 连接端口
     */
    protected Iec102PrimaryStation(String host, int port) {
        super(host, port, new Iec102PrimaryAbstractFactory());
    }

}