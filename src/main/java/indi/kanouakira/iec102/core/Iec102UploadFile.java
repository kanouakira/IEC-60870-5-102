package indi.kanouakira.iec102.core;

import indi.kanouakira.iec102.core.enums.TypeIdentificationEnum;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static indi.kanouakira.iec102.core.Iec102Constant.FILENAME_BYTE_LENGTH;

/**
 * 待上报的E文本。
 *
 * @author KanouAkira
 * @date 2022/4/28 12:10
 */
public class Iec102UploadFile {
    // 文件类型
    private TypeIdentificationEnum typeIdentificationEnum;

    // 文件名
    private String fileName;

    // 文件名字节数组
    private byte[] fileNameBytes;

    // 文件内容
    private byte[] fileContext;

    // 失效时间，用于取消待上报状态
    private Date expireAt;

    // 线程已上传变量副本，用于不同Iec102连接是否上传过这个E文本的标识
    private ThreadLocal<Boolean> localUploaded = ThreadLocal.withInitial(() -> false);

    // 线程已读取长度副本，用于分段读取
    private ThreadLocal<Integer> localReadIndex = ThreadLocal.withInitial(() -> 0);

    /**
     * 创建待上报文件
     *
     * @param typeIdentificationEnum 上报文件的类型标识
     * @param fileName               上报文件名
     * @param fileContext            上报文件内容
     * @param expireAt               上报有效截止时间
     * @return
     */
    public static Iec102UploadFile createUploadFile(TypeIdentificationEnum typeIdentificationEnum, String fileName, byte[] fileContext, Date expireAt) {
        if (typeIdentificationEnum == null || fileName == null || fileContext == null || expireAt == null)
            throw new IllegalArgumentException();
        if (fileName.getBytes(StandardCharsets.UTF_8).length > FILENAME_BYTE_LENGTH)
            throw new IllegalArgumentException("文件名过长");
        return new Iec102UploadFile(typeIdentificationEnum, fileName, fileContext, expireAt);
    }

    protected Iec102UploadFile(TypeIdentificationEnum typeIdentificationEnum, String fileName, byte[] fileContext, Date expireAt) {
        this.typeIdentificationEnum = typeIdentificationEnum;
        this.fileName = fileName;
        this.fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        this.fileContext = fileContext;
        this.expireAt = expireAt;
    }

    /**
     * 获取发送文件报文中约定的用户数据部分，即第一部分64字节文件名，第二部分文件内容。
     * @param length 读取文件内容的字节长度
     * @return 如果上次已读完所有内容且没重置readIndex，则返回null
     */
    public byte[] readDataBytes(int length){
        Integer lastReadIndex = localReadIndex.get();
        if (lastReadIndex == fileContext.length)
            return null;
        int restLength = fileContext.length - lastReadIndex;
        length = length > restLength ? restLength : length;

        byte[] data = new byte[FILENAME_BYTE_LENGTH + length];
        System.arraycopy(fileNameBytes, 0, data, 0, fileNameBytes.length);
        System.arraycopy(fileContext, lastReadIndex, data, FILENAME_BYTE_LENGTH, length);
        localReadIndex.set(lastReadIndex + length);
        return data;
    }

    public void resetReadIndex(){
        localReadIndex.set(0);
    }

    public TypeIdentificationEnum getTypeIdentificationEnum() {
        return typeIdentificationEnum;
    }

    /**
     * 检查是否未上传。
     * @return
     */
    public boolean isNotUploaded(){
        return !localUploaded.get();
    }

    /**
     * 设置已经上送完毕。
     */
    public void setUploadComplete(){
        localUploaded.set(true);
    }

    /**
     * 重置上传完毕的状态，只有在需要重发时调用。
     */
    public void resetUploaded(){
        localUploaded.set(false);
    }

    /**
     * 检查是否已经超过需要上送的时间节点。
     * @param millis 当前时间戳
     * @return
     */
    public boolean isNotExpired(long millis){
        return expireAt.getTime() > millis;
    }

    public boolean isNotExpired(){
        return isNotExpired(System.currentTimeMillis());
    }

    /**
     * 获取文件长度。
     * @return
     */
    public int getDataLength(){
        return fileContext.length;
    }

    public String getFileName() {
        return fileName;
    }

    public void clear(){
        localReadIndex.remove();
        localUploaded.remove();
    }

}
