package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.standard.StandardProtocolEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 主站工厂。
 *
 * @author KanouAkira
 * @date 2022/5/23 11:26
 */
public class PrimaryStationFactory {

    final static Logger logger = LoggerFactory.getLogger(PrimaryStationFactory.class);

    public static PrimaryStation create(String host, int port, StandardProtocolEnum protocol) {
        PrimaryStation station = switch (protocol) {
            case IEC102 -> new Iec102PrimaryStation(host, port);
            default -> throw new IllegalStateException("未知协议");
        };
        logger.info("创建主站，使用协议：{}", protocol.name());
        return station;
    }

}
