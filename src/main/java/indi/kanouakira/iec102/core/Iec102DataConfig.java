package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.standard.DataConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * IEC 102 的数据配置类。
 *
 * @author KanouAkira
 * @date 2022/4/21 11:09
 */
public class Iec102DataConfig implements DataConfig {

    /* 线程变量副本 */
    private static Iec102DataConfig config = null;

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

    /**
     * 移除过期待上送文件。
     */
    public static void removeExpireFile(){
        Iterator<Iec102UploadFile> iterator = waitingForUpload.iterator();
        long currentTimeMillis = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Iec102UploadFile next = iterator.next();
            if (!next.isNotExpired(currentTimeMillis)){
                iterator.remove();
            }
        }
    }

    public static boolean addFile(Iec102UploadFile file) {
        if (file == null)
            return false;
        return waitingForUpload.add(file);
    }

    public byte getAddressLength() {
        return addressSpecification.length;
    }

    public byte[] getTerminalAddress() {
        return terminalAddress;
    }

}
