package indi.kanouakira.iec102.core.iec104;

import indi.kanouakira.iec102.core.iec104.enums.QualifiersEnum;
import indi.kanouakira.iec102.core.iec104.enums.TypeIdentifierEnum;
import indi.kanouakira.iec102.core.iec104.enums.UControlEnum;
import indi.kanouakira.iec102.util.ByteUtil;
import indi.kanouakira.iec102.util.Iec104Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 104 规约的基本指令封装
 * @ClassName: BasicInstruction104
 * @Description: 返回指定的指令
 * @author YDL
 */
public class BasicInstruction104 {

	private static BasicInstruction104 basicInstruction104;

	/**
	 * 初始确认指令
	 */
	public static final byte[] STARTDT_YES = new byte[] {0x68, 0x04, 0x0B, 0x00, 0x00, 0x00};
	
	/**
	 * 链路启动指令
	 */
	public static final byte[] STARTDT = new byte[] {0x68, 0x04, 0x07, 0x00, 0x00, 0x00};

	/**
	 * 测试确认
	 */
	public static final byte[] TESTFR_YES = new byte[] {0x68, 0x04, (byte) 0x83, 0x00, 0x00, 0x00};
	 
	/**
	 * 测试命令指令
	 */
	public static final byte[] TESTFR = new byte[] {0x68, 0x04, (byte) 0x43, 0x00, 0x00, 0x00};

	/**
	 * 停止确认
	 */
	public static final byte[] STOPDT_YES = new byte[] {0x68, 0x04, 0x23, 0x00, 0x00, 0x00};

	/**
	 * @Title: getGeneralCallRuleDetail104
	 * @Description: 总召唤指令
	 * @param @return
	 * @param @throws IOException
	 * @return Iec104MessageDetail
	 * @throws
	 */
	public static Iec104MessageDetail getGeneralCallRuleDetail104() {
		TypeIdentifierEnum typeIdentifierEnum = TypeIdentifierEnum.generalCall;
		int sq = 0;
		boolean isContinuous = sq == 0 ? false : true;
		// 接收序号
		short accept = 0;
		// 发送序号
		short send = 0;
		byte[] control = Iec104Util.getIControl(accept, send);
		// 传输原因
		short transferReason = 6;
		boolean isTest = false;
		boolean isPn = true;
		// 终端地址 实际发生的时候会被替换
		short terminalAddress = 1;
		// 消息地址 总召唤地址为0
		int messageAddress = 0;
		QualifiersEnum qualifiers = QualifiersEnum.generalCallGroupingQualifiers;
		List<Iec104MessageInfo> messages = new ArrayList<>();
		Iec104MessageInfo message = new Iec104MessageInfo();
		message.setQualifiersType(qualifiers);
		message.setMessageInfos(new byte[] {});
		messages.add(message);
		Iec104MessageDetail ruleDetail104 = new Iec104MessageDetail(control, typeIdentifierEnum, isContinuous, isTest, isPn, transferReason,
				terminalAddress, messageAddress, messages, null, qualifiers);
		return ruleDetail104;
	}

	/**
	 * 
	 * @Title: getYesGeneralCallRuleDetail104
	 * @Description: 总召唤确认指令
	 * @return
	 * @return Iec104MessageDetail
	 * @throws
	 */
	public static Iec104MessageDetail getYesGeneralCallRuleDetail104() {
		TypeIdentifierEnum typeIdentifierEnum = TypeIdentifierEnum.generalCall; 
		 //SQ=0 length =1
		int sq = 0;
		boolean isContinuous = sq == 0 ? false : true;
		// 接收序号
		short accept = 0;
		// 发送序号
		short send = 0;
		byte[] control = Iec104Util.getIControl(accept, send);
		// 传输原因
		short transferReason = 7;
		// true：1 ; false ： 0
		boolean isTest = false;
		// true:0 false;1
		boolean isPN = true;
		
		short terminalAddress = 1;
		// 消息地址 总召唤地址为0
		int messageAddress = 0;
		
		QualifiersEnum qualifiers = QualifiersEnum.generalCallGroupingQualifiers;
		List<Iec104MessageInfo> messages = new ArrayList<>();
		Iec104MessageInfo message = new Iec104MessageInfo();
		message.setQualifiersType(qualifiers);
		message.setMessageInfos(new byte[] {});
		
		messages.add(message);
		Iec104MessageDetail ruleDetail104 = new Iec104MessageDetail(control, typeIdentifierEnum, isContinuous, isTest, isPN, transferReason,
				terminalAddress, messageAddress, messages, null, qualifiers);
		return ruleDetail104;
	}

	/**
	 * @Title: getEndGeneralCallRuleDetail104
	 * @Description: 总召唤结束指令
	 * @return
	 * @return Iec104MessageDetail
	 * @throws
	 */
	public static Iec104MessageDetail getEndGeneralCallRuleDetail104() {
		TypeIdentifierEnum typeIdentifierEnum = TypeIdentifierEnum.generalCall; 
		 //SQ=0 length =1
		int sq = 0;
		boolean isContinuous = sq == 0 ? false : true;
		// 接收序号
		short accept = 1;
		// 发送序号
		short send = 4;
		byte[] control = Iec104Util.getIControl(accept, send);
		// 传输原因
		short transferReason = 0x0A;
		// true：1 ; false ： 0
		boolean isTest = false;
		// true:0 false;1
		boolean isPN = true;
		
		short terminalAddress = 1;
		// 消息地址 总召唤地址为0
		int messageAddress = 0;
		// 老板限定词
		QualifiersEnum qualifiers = QualifiersEnum.generalCallGroupingQualifiers;
		List<Iec104MessageInfo> messages = new ArrayList<>();
		Iec104MessageInfo message = new Iec104MessageInfo();
		message.setQualifiersType(qualifiers);
		message.setMessageInfos(new byte[] {});
		
		messages.add(message);
		Iec104MessageDetail ruleDetail104 = new Iec104MessageDetail(control, typeIdentifierEnum, isContinuous, isTest, isPN, transferReason,
				terminalAddress, messageAddress, messages, null, qualifiers);
		return ruleDetail104;
	}

	/**
	 * 自定义传输内容相应总召返回气象站设备的Message Detail
	 * @return
	 */
	public static Iec104MessageDetail customInfo(){
		TypeIdentifierEnum typeIdentifierEnum = TypeIdentifierEnum.shortFloatingPointTelemetry;
		//SQ=0 length =1
		int sq = 1;
		boolean isContinuous = sq == 0 ? false : true;
		// 接收序号
		short accept = 1;
		// 发送序号
		short send = 4;
		byte[] control = Iec104Util.getIControl(accept, send);
		// 传输原因
		short transferReason = 0x14;
		// true：1 ; false ： 0
		boolean isTest = false;
		// true:0 false;1
		boolean isPN = true;
		short terminalAddress = 1;
		int messageAddress = 16385;
		// 老板限定词
		QualifiersEnum qualifiers = QualifiersEnum.qualityQualifiers;
		List<Iec104MessageInfo> messages = new ArrayList<>();

		for (int i = 16385; i <= 16391; i++) {
			Object result = 100;
			Iec104MessageInfo message = new Iec104MessageInfo();
			if (result != null){
				byte[] byteArray = ByteUtil.getByteArray((Float) result);
				message.setMessageInfos(ByteUtil.reverse(byteArray));
			}else{
				byte[] byteArray = ByteUtil.getByteArray((float) 0.0);
				message.setMessageInfos(ByteUtil.reverse(byteArray)); // 数据缺失
			}
			messages.add(message);
		}

		Iec104MessageDetail ruleDetail104 = new Iec104MessageDetail(control, typeIdentifierEnum, isContinuous, isTest, isPN, transferReason,
				terminalAddress, messageAddress, messages, null, qualifiers);
		return ruleDetail104;
	}


	public static Iec104MessageDetail getInitRuleDetail104() {
		byte[] control = ByteUtil.intToByteArray(UControlEnum.STARTDT.getValue());
		Iec104MessageDetail ruleDetail104 = new Iec104MessageDetail(control);
		return ruleDetail104;
	}
}
