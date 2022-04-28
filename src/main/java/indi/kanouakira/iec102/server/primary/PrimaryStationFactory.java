package indi.kanouakira.iec102.server.primary;

import indi.kanouakira.iec102.standard.StandardProtocolEnum;

/**
 * 主站工厂
 *
 * @author KanouAkira
 * @date 2022/4/19 15:12
 */
public class PrimaryStationFactory {

    public static PrimaryStation createPrimaryStation(String host, int port, StandardProtocolEnum protocol){
        PrimaryStation station = switch (protocol){
            case IEC102 -> new Iec102PrimaryStation(host, port);
            case IEC104 -> new Iec104PrimaryStation(host, port);
        };
        return station;
    }

}
