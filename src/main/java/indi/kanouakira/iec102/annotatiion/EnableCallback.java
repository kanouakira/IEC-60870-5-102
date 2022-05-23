package indi.kanouakira.iec102.annotatiion;

import indi.kanouakira.iec102.config.CallbackConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author KanouAkira
 * @date 2022/5/23 10:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({CallbackConfiguration.class})
public @interface EnableCallback {
}
