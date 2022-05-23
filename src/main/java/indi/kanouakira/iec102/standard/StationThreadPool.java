package indi.kanouakira.iec102.standard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 站端线程池
 *
 * @author KanouAkira
 * @date 2022/5/23 10:24
 */
public class StationThreadPool {

    private static StationThreadPool stationThreadPool = new StationThreadPool();
    private ExecutorService executorService;

    private StationThreadPool() {
        executorService = Executors.newCachedThreadPool();
    }

    public static void execute(Runnable runnable) {
        stationThreadPool.executorService.execute(runnable);
    }

}
