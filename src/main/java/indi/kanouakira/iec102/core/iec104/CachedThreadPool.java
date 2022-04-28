package indi.kanouakira.iec102.core.iec104;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程池
 */
// TODO: 2021/8/27 未知为何使用该线程池创建线程，目前认为与 newThread 无区别 
public final class CachedThreadPool {

    private static CachedThreadPool cachedThreadPool = new CachedThreadPool();
    private ExecutorService executorService;

    private CachedThreadPool() {
        executorService = Executors.newCachedThreadPool();
    }

    public static CachedThreadPool getCachedThreadPool() {
        return cachedThreadPool;
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

}
