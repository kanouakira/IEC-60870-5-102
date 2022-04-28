package indi.kanouakira.iec102.core.iec104.enums;


/**
 * 
* @ClassName: TypeIdentifierEnum   类型标识
* @author YDL
* @date 2020年5月13日
 */
public enum TypeIdentifierEnum {

	/**
	 * 单点摇信
	 */
	onePointTeleIndication(0x01, 1),

	/**
	 * 双点摇信
	 */
	twoPointTeleIndication(0x03, 1),

	/**
	 * 测量值 归一化值 遥测
	 */
	normalizedTelemetry(0x09,2),

	/**
	 * 测量值  标度化值 遥测
	 */
	scaledTelemetry(0x0B, 2),

	/**
	 * 测量值 短浮点数 遥测 Short floating point
	 */
	shortFloatingPointTelemetry(0x0D, 4),

	/**
	 * 摇信带时标 单点
	 */
	onePointTimeTeleIndication(0x1E, 1),

	/**
	 * 摇信带时标 双点
	 */
	twoPointTimeTeleIndication(0x1F, 1),

	/**
	 * 单命令 遥控
	 */
	onePointTeleControl(0x2D, 1),

	/**
	 * 双命令遥控
	 */
	twoPointTeleControl(0x2E, 1),

	/**
	 * 读单个参数
	 */
	readOneParameter(0x66, 4),

	/**
	 * 读多个参数
	 */
	readMultipleParameter(0x84, 4),

	/**
	 * 预置单个参数命令
	 */
	prefabActivationOneParameter(0x30, 4),

	/**
	 * 预置多个个参数
	 */
	prefabActivationMultipleParameter(0x88, 4),

	/**
	 * 初始化结束
	 */
	initEnd(0x46, 0),

	/**
	 * 召唤命令
	 */
	generalCall(0x64, 0),

	/**
	 * 时钟同步
	 */
	timeSynchronization(0x67, 0),

	/**
	 * 复位进程
	 */
	resetProcess(0x69, 0);

	private byte value;
	private int messageLength;

	public byte getValue() {
		return value;
	}

	public int getMessageLength() {
		return messageLength;
	}

	TypeIdentifierEnum(int value, int messageLength) {
		this.value = (byte) value;
		this.messageLength = messageLength;
	}

	public static TypeIdentifierEnum getTypeIdentifierEnum(byte value) {
		for (TypeIdentifierEnum type : TypeIdentifierEnum.values()) {
			if (type.getValue() == value) {
				return type;
			}
		}
		return null;
	}

	public static boolean isTelemetry(TypeIdentifierEnum typeIdentifierEnum) {
		return TypeIdentifierEnum.normalizedTelemetry == typeIdentifierEnum
				|| scaledTelemetry.shortFloatingPointTelemetry == typeIdentifierEnum;
	}

}
