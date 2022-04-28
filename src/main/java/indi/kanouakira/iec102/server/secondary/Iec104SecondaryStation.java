package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.core.iec104.Iec104SecondaryAbstractFactory;

/**
 * @author KanouAkira
 * @date 2022/4/18 20:28
 */
public class Iec104SecondaryStation extends SecondaryStation{

    protected Iec104SecondaryStation(int port) {
        super(port, new Iec104SecondaryAbstractFactory());
    }

}
