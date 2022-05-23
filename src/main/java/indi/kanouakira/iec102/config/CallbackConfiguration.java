package indi.kanouakira.iec102.config;

import indi.kanouakira.iec102.core.Iec102Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 将上下文工具注入容器。
 *
 * @author KanouAkira
 * @date 2022/5/23 10:12
 */
@Configuration
@ComponentScan("indi.kanouakira.iec102")
public class CallbackConfiguration {

    @Value("${iec102.filename-length:64}")
    public void setFilenameByteLength(int filenameByteLength) {
        Iec102Constant.FILENAME_BYTE_LENGTH = filenameByteLength;
    }

}
