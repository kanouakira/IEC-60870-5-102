package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.core.iec104.enums.QualifiersEnum;

/**
 * 报文中 的消息部分
 */
public class Iec104MessageInfo {
	/**
	 * 消息地址 字节
	 */
	private int messageAddress;
	
	/**
	 * 信息元素集合 1 2 4 个字节
	 */
	private byte[] messageInfos;

	/**
	 * 限定词
	 */
	private QualifiersEnum qualifiersType;

	/**
	 * 消息详情
	 */
	private int messageInfoLength;

	public int getMessageAddress() {
		return messageAddress;
	}

	public void setMessageAddress(int messageAddress) {
		this.messageAddress = messageAddress;
	}

	public byte[] getMessageInfos() {
		return messageInfos;
	}

	public void setMessageInfos(byte[] messageInfos) {
		this.messageInfos = messageInfos;
	}

	public QualifiersEnum getQualifiersType() {
		return qualifiersType;
	}

	public void setQualifiersType(QualifiersEnum qualifiersType) {
		this.qualifiersType = qualifiersType;
	}

	public int getMessageInfoLength() {
		return messageInfoLength;
	}

	public void setMessageInfoLength(int messageInfoLength) {
		this.messageInfoLength = messageInfoLength;
	}

}
