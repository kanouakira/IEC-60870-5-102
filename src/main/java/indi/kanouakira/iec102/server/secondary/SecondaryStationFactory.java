package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.standard.StandardProtocolEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从站工厂
 *
 * @author KanouAkira
 * @date 2022/4/19 15:12
 */
public class SecondaryStationFactory {

    final static Logger logger = LoggerFactory.getLogger(SecondaryStationFactory.class);

    public static SecondaryStation create(int port, StandardProtocolEnum protocol) {
        SecondaryStation station = switch (protocol) {
            case IEC102 -> new Iec102SecondaryStation(port);
            default -> throw new IllegalStateException("未知协议");
        };
        logger.info("创建从站，使用协议：{}", protocol.name());
        return station;
    }

}
