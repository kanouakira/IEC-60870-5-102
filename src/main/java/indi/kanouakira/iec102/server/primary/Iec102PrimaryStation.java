package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.core.iec104.Iec104PrimaryAbstractFactory;

/**
 * @author KanouAkira
 * @date 2022/4/18 20:22
 */
public class Iec102PrimaryStation extends PrimaryStation {

    public Iec102PrimaryStation(String host, int port) {
        super(host, port, new Iec104PrimaryAbstractFactory());
    }

    public static void main(String[] args) throws Exception {
        new Iec102PrimaryStation("127.0.0.1", 2404).run();
    }

}
