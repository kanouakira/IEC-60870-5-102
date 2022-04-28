package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.standard.StandardProtocolEnum;

/**
 * 从站工厂
 *
 * @author KanouAkira
 * @date 2022/4/19 15:12
 */
public class SecondaryStationFactory {

    public static SecondaryStation createSecondaryStation(int port, StandardProtocolEnum protocol){
        SecondaryStation station = switch (protocol){
            case IEC102 -> new Iec102SecondaryStation(port);
            case IEC104 -> null;
            default -> throw new IllegalStateException("未知协议");
        };
        return station;
    }

}
