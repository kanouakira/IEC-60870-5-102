package indi.kanouakira.iec102.core;

/**
 * 文件传输结果回调接口，如需获取传输结果，实现该接口。
 *
 * @author KanouAkira
 * @date 2022/5/23 10:17
 */
public interface Iec102Callback {

    /**
     * 传输结果回调函数。
     *
     * @param fileName 上传时UploadFile的文件名
     * @param result   上传结果
     */
    void callback(String fileName, boolean result);

}
