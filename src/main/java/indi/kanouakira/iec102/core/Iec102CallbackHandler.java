package indi.kanouakira.iec102.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author KanouAkira
 * @date 2022/5/23 10:18
 */
public class Iec102CallbackHandler {

    final static Logger logger = LoggerFactory.getLogger(Iec102CallbackHandler.class);

    private Set<String> completedRecord = new HashSet<>();

    @Autowired(required = false)
    private Iec102Callback iec102Callback;

    public void handleResult(Iec102UploadFile file) {
        if (iec102Callback != null) {
            boolean result = false;
            String fileName = file.getFileName();
            if (file.isNotExpired()) {
                result = true;
                completedRecord.add(fileName);
            } else if (completedRecord.contains(fileName)) {
                // 过期回调,检查是否回调过，回调过无视，否则回调失败
                completedRecord.remove(fileName);
                return;
            }
            iec102Callback.callback(fileName, result);
        } else {
            logger.warn("开启了回调注解，但无回调实现。");
        }
    }

}
