package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.standard.DataConfig;

import java.util.*;

/**
 * @author KanouAkira
 * @date 2022/4/21 11:09
 */
public class Iec102DataConfig implements DataConfig {

    /* 线程变量副本 */
    private static Iec102DataConfig config = null;

//    /* FCB计数线程变量副本 */
//    private static ThreadLocal<Byte> localFcb = ThreadLocal.withInitial(()-> (byte)0x00);

    /* 待上报文件，key:文件名，value：文件字节数组 */
    protected static List<Iec102UploadFile> waitingForUpload = new ArrayList<>();

    /* 地址长度规格 */
    private AddressEnum addressSpecification;

    /* 终端地址 */
    private byte[] terminalAddress;

    public Iec102DataConfig(AddressEnum addressSpecification, byte[] terminalAddress) {
        if (addressSpecification.length != terminalAddress.length)
            throw new IllegalStateException("终端地址长度与规格不匹配");
        this.addressSpecification = addressSpecification;
        this.terminalAddress = terminalAddress;
        Iec102DataConfig.config = this;
    }

//    public static byte getFcb() {
//        return localFcb.get();
//    }
//
//    public static void setFcb(byte fcb){
//        localFcb.set(fcb);
//    }

    public static Iec102DataConfig getConfig() {
        return Iec102DataConfig.config;
    }

    public enum AddressEnum {
        ZERO_BYTE(0x00),
        SINGLE_BYTE(0x01),
        DOUBLE_BYTE(0x02);

        private byte length;

        AddressEnum(int length) {
            this.length = (byte) length;
        }
    }

    public static List<Iec102UploadFile> getWaitingForUpload() {
        return Collections.unmodifiableList(waitingForUpload);
    }

    public static void addFile(Iec102UploadFile file){
        waitingForUpload.add(file);
    }

    public byte getAddressLength(){
        return addressSpecification.length;
    }

    public byte[] getTerminalAddress(){
        return terminalAddress;
    }

}
