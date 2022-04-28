package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.core.Iec102SecondaryAbstractFactory;

/**
 * @author KanouAkira
 * @date 2022/4/18 20:28
 */
public class Iec102SecondaryStation extends SecondaryStation {
    public Iec102SecondaryStation(int port) {
        super(port, new Iec102SecondaryAbstractFactory());
    }

}