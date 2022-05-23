package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.core.Iec102DataConfig;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * @author KanouAkira
 * @date 2022/4/18 20:28
 */
public class Iec102SecondaryStation extends SecondaryStation {
    public Iec102SecondaryStation(int port) {
        super(port, new Iec102SecondaryAbstractFactory());
        // 定时回收过期的上送文件
        newScheduledThreadPool(1).scheduleWithFixedDelay(Iec102DataConfig::removeExpireFile, 30, 60, TimeUnit.SECONDS);
    }

}