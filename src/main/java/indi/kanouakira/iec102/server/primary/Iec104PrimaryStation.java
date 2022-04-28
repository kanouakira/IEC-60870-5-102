package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.core.iec104.Iec104PrimaryAbstractFactory;

/**
 * IEC 60870-5-104 标准实现主站。
 * @author KanouAkira
 * @date 2022/4/18 20:22
 */
public class Iec104PrimaryStation extends PrimaryStation {

    public Iec104PrimaryStation(String host, int port) {
        super(host, port, new Iec104PrimaryAbstractFactory());
    }

}
