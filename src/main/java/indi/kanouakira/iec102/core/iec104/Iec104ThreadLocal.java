package indi.kanouakira.iec102.core.iec104;


import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:  Iec104ThreadLocal
 * @Description:  线程变量管理
 * @author: YDL
 * @date:   2020年5月19日 上午10:48:57
 */
public class Iec104ThreadLocal {
	
	/**
	 * 定时发送启动链指令 测试链指令 
	 */
	private static ThreadLocal<ScheduledTaskPool> scheduledTaskPoolThreadLocal = new ThreadLocal<>();
	
	/**
	 * 返回发送序号和接收序号 定时发送S帧
	 */
	private static ThreadLocal<Map<String,ControlManageUtil>> controlPoolThreadLocal = ThreadLocal.withInitial(() -> new HashMap<>());

	/**
	 * 存放相关配置文件
	 */
	private static ThreadLocal<Iec104Config> iec104ConfigThreadLocal = new ThreadLocal<>();

	public static void setScheduledTaskPool(ScheduledTaskPool scheduledTaskPool) {
		scheduledTaskPoolThreadLocal.set(scheduledTaskPool);
	}

	public static ScheduledTaskPool getScheduledTaskPool() {
		return scheduledTaskPoolThreadLocal.get();
	}

	public static void setControlPool(String key,ControlManageUtil controlPool) {
		Map<String, ControlManageUtil> controlMap = controlPoolThreadLocal.get();
		controlMap.put(key, controlPool);
		controlPoolThreadLocal.set(controlMap);
	}

	public static ControlManageUtil getControlPool(String key) {
		Map<String, ControlManageUtil> controlMap = controlPoolThreadLocal.get();
		return controlMap.get(key);
	}

	public static ControlManageUtil removeControlPool(String key) {
		Map<String, ControlManageUtil> controlMap = controlPoolThreadLocal.get();
		return controlMap.remove(key);
	}

	public static Iec104Config getIec104Config() {
		return iec104ConfigThreadLocal.get();
	}

	public static void setIec104Config(Iec104Config iec104Config) {
		iec104ConfigThreadLocal.set(iec104Config);
	}

}
