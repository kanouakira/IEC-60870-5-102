package indi.kanouakira.iec102.core.enums;

import indi.kanouakira.iec102.util.ByteUtil;

import java.io.*;

/**
 * IEC 60870-5-102 类型标识枚举。
 *
 * @author KanouAkira
 * @date 2022/4/20 16:36
 */
public enum TypeIdentificationEnum {

    /* IEC 60870-5-102 类型标识 这里不使用所以不列举。
    监控器方向上的处理信息
        <0> := Not defined
        <1> := Single-point information with time tag M_SP_TA_2
        <2> := Accounting integrated totals, four octets each M_IT_TA_2
        <3> := Accounting integrated totals, three octets each M_IT_TB_2
        <4> := Accounting integrated totals, two octets each M_IT_TC_2
        <5> := Periodically reset accounting integrated totals, four octets each M_IT_TD_2
        <6> := Periodically reset accounting integrated totals, three octets each M_IT_TE_2
        <7> := Periodically reset accounting integrated totals, two octets each M_IT_TF_2
        <8> := Operational integrated totals, four octets each M_IT_TG_2
        <9> := Operational integrated totals, three octets each M_IT_TH_2
        <10> := Operational integrated totals, two octets each M_IT_TI_2
        <11> := Periodically reset operational integrated totals, four octets each M_IT_TK_2
        <12> := Periodically reset operational integrated totals, three octets each M_IT_TL_2
        <13> := Periodically reset operational integrated totals, two octets each M_IT_TM_2
        <14..69> := reserved for further compatible definitions
    监控器方向上的系统信息
        <70> := End of initialization M_EI_NA_2
        <71> := Manufacturer and product specification of integrated total DTE P_MP_NA_2
        <72> := Current system time of integrated total DTE M_TI_TA_2
        <73..99> := reserved for further compatible definitions
    在控制方向上的系统信息
        CON<100> := Read manufacturer and product specification C_RD_NA_2
        CON<101> := Read record of single-point information with time tag C_SP_NA_2
        CON<102> := Read record of single-point information with time tag of a selected time range C_SP_NB_2
        CON<103> := Read current system time of integrated total DTE C_TI_NA_2
        CON<104> := Read accounting integrated totals of the oldest integration period C_CI_NA_2
        CON<105> := Read accounting integrated totals of the oldest integration period and of a selected range of addresses C_CI_NB_2
        CON<106> := Read accounting integrated totals of a specific past integration period C_CI_NC_2
        CON<107> := Read accounting integrated totals of a specific past integration period and of a selected range of addresses C_CI_ND_2
        CON<108> := Read periodically reset accounting integrated totals of the oldest integration period C_CI_NE_2
        CON<109> := Read periodically reset accounting integrated totals of the oldest integration period and of a selected range of addresses C_CI_NF_2
        CON<110> := Read periodically reset accounting integrated totals of a specific past integration period C_CI_NG_2
        CON<111> := Read periodically reset accounting integrated totals of a specific past integration period and of a selected range of addresses C_CI_NH_2
        CON<112> := Read operational integrated totals of the oldest integration period C_CI_NI_2
        CON<113> := Read operational integrated totals of the oldest integration period and of a selected range of addresses C_CI_NK_2
        CON<114> := Read operational integrated totals of a specific past integration period C_CI_NL_2
        CON<115> := Read operational integrated totals of a specific past integration period and of a selected range of addresses C_CI_NM_2
        CON<116> := Read periodically reset operational integrated totals of the oldest integration period C_CI_NN_2
        CON<117> := Read periodically reset operational integrated totals of the oldest integration period and of a selected range of addresses C_CI_NO_2
        CON<118> := Read periodically reset operational integrated totals of a specific past integration period C_CI_NP_2
        CON<119> := Read periodically reset operational integrated totals of a specific past integration period and of a selected range of addresses C_CI_NQ_2
        CON<120> := Read accounting integrated totals of a selected time range and of a selected range of addresses C_CI_NR_2
        CON<121> := Read periodically reset accounting integrated totals of a selected time range and of a selected range of addresses C_CI_NS_2
        CON<122> := Read operational integrated totals of a selected time range and of a selected range of addresses C_CI_NT_2
        CON<123> := Read periodically reset operational integrated totals of a selected time range and of a selected range of addresses C_CI_NU_2
        <124..127> := reserved for further compatible definitions
     */

    /* 以下为自定义拓展内容 */

    /* 短期预测文件 */
    DQYC(0x90),

    /* 超短期预测文件 */
    CDQYC(0x91),

    /* 风电场总体信息/气象环境监视信息报文 */
    ZTXX_QXHJ(0x93),

    /* 升压站信息 */
    SYZXX(0x9B),

    /* 风力发电机组信息、或光伏逆变器/汇流箱 信息报文 */
    FDJZ(0x9C),

    /* AGC-AVC */
    AGC_AVC(0x9D),
    /* 风电场统计信息/光伏电站统计信息 */
    TJXX(0x9E),

    /* 光伏箱变/方阵信息 */
    XB_FZ(0xAD),

    /* 光伏电站总体信息-气象环境-太阳跟踪系统信息 */
    ZTXX_QXHJ_TYGZ(0xA0),

    /* 光伏逆变器/汇流箱信息 */
    NBQ_HLX(0xA1);


    TypeIdentificationEnum(int value) {
        this.value = (byte) value;
    }

    private byte value;

    public byte getValue() {
        return value;
    }

    public static void main(String[] args) throws IOException {
        String s = "47 46 5F 47 5A 2E 5A 68 6F 6E 67 42 44 43 5F 58 42 2D 46 5A 5F 32 30 32 31 31 31 31 35 5F 31 30 31 35 30 30 2E 64 61 74";
        String s2 = "47 46 5F 47 5A 2E 5A 68 6F 6E 67 42 44 43 5F 58 42 2D 46 5A 5F 32 30 32 31 31 31 31 35 5F 31 30 30 30 30 30 2E 64 61 74";
        byte[] bytes = ByteUtil.hexStringToBytes(s2);
        String s1 = new String(bytes, "UTF-8");
        System.out.println(s1);
        String fileName = "47 46 5F 47 5A 2E 5A 68 6F 6E 67 42 44 43 5F 58 42 2D 46 5A 5F 32 30 32 31 31 31 31 35 5F 31 30 31 35 30 30 2E 64 61 74 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00";

        int length = 299;
        String fileContext = "3C 21 53 79 73 74 65 6D 3D 4F 4D 53 09 56 65 72 73 69 6F 6E 3D 31 2E 30 09 43 6F 64 65 3D 55 54 46 2D 38 09 44 61 74 61 3D 31 2E 30 21 3E 0A 2F 2F B9 E2 B7 FC CF E4 B1 E4 BC E0 B2 E2 0A 3C 58 42 3A 3A 47 5A 2E 5A 68 6F 6E 67 42 44 43 09 44 61 74 65 3D 27 32 30 32 31 2D 31 31 2D 31 35 27 09 54 69 6D 65 3D 27 31 30 2D 31 35 2D 30 30 27 3E 0A 40 09 D0 F2 BA C5 09 CF E4 B1 E4 B1 E0 BA C5 09 CF E4 B1 E4 B5 CD D1 B9 B2 E0 B5 E7 D1 B9 55 61 09 CF E4 B1 E4 B5 CD D1 B9 B2 E0 B5 E7 C1 F7 49 61 09 CF E4 B1 E4 B5 CD D1 B9 B2 E0 D3 D0 B9 A6 B9 A6 C2 CA 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 D1 B9 55 61 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 D1 B9 55 62 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 D1 B9 55 63 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 D1 B9 55 61 62 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 D1 B9 55 62 63 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 D1 B9 55 63 61 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7";
        byte[] bytes1 = ByteUtil.hexStringToBytes(fileContext);
        System.out.println(bytes1.length);
        int length1 = 299;
        String fileContext1 = "C1 F7 49 61 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 C1 F7 49 62 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B5 E7 C1 F7 49 63 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 D3 D0 B9 A6 B9 A6 C2 CA 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 CE DE B9 A6 B9 A6 C2 CA 09 CF E4 B1 E4 B8 DF D1 B9 B2 E0 B9 A6 C2 CA D2 F2 CA FD 09 CF E4 B1 E4 C8 C6 D7 E9 CE C2 B6 C8 09 C8 D5 B7 A2 B5 E7 C1 BF 09 D4 C2 B7 A2 B5 E7 C1 BF 09 C4 EA B7 A2 B5 E7 C1 BF 09 C0 DB BC C6 B7 A2 B5 E7 C1 BF 09 CF E4 B1 E4 A3 A8 B7 BD D5 F3 A3 A9 C0 ED C2 DB D7 EE B4 F3 D3 D0 B9 A6 B9 A6 C2 CA 09 CF E4 B1 E4 CE DE B9 A6 CA E4 B3 F6 B7 B6 CE A7 09 CF E4 B1 E4 B2 A2 CD F8 D4 CB D0 D0 D7 B4 CC AC 09 CF E4 B1 E4 B9 CA D5 CF D7 B4 CC AC 09 CF E4 B1 E4 BC EC D0 DE 09 CF E4 B1 E4 CF DE B5 E7 09 D6 B1 C1 F7 B9 FD D1 B9 09 BD BB C1 F7 B9 FD D1 B9 09 BD BB C1 F7 C7 B7 D1 B9 09 78 78 B1 A3 BB A4 B6 AF D7 F7 BC B0 B8 E6 BE AF D0 C5 BA C5 09 0A 23 09 31 09 23 31 09 32 31 2E";
        byte[] bytes2 = ByteUtil.hexStringToBytes(fileContext1);
        System.out.println(bytes2.length);
        int length2 = 299;
        String fileContext2 = "36 30 37 38 33 39 35 38 34 33 35 30 36 09 31 32 38 2E 31 31 39 35 33 37 33 35 33 35 31 36 09 38 2E 30 37 31 37 34 33 30 31 31 34 37 34 36 31 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 0A 23 09 32 09 23 32 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C";
        byte[] bytes3 = ByteUtil.hexStringToBytes(fileContext2);
        System.out.println(bytes3.length);
        int length3 = 299;
        String fileContext3 = "09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 0A 23 09 33 09 23 33 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 0A 3C 2F 58 42 3A 3A 47 5A 2E 5A 68 6F 6E 67 42 44 43 3E 0A 0A 2F 2F B7 BD D5 F3 BC E0 B2 E2 0A 3C 46 5A 3A 3A 47 5A 2E 5A 68 6F 6E 67 42 44 43 09 44 61 74 65 3D 27 32 30 32 31 2D 31 31 2D 31 35 27 09 54 69 6D 65 3D 27 31 30 2D 31 35 2D 30 30 27 3E 0A 40 09 D0 F2 BA C5 09 B7 BD D5 F3 B1 E0 BA C5 09 B7";
        byte[] bytes4 = ByteUtil.hexStringToBytes(fileContext3);
        System.out.println(bytes4.length);
        String fileContext4 = "BD D5 F3 D6 B1 C1 F7 CA E4 C8 EB B5 E7 C1 F7 09 B7 BD D5 F3 D6 B1 C1 F7 CA E4 B3 F6 B5 E7 C1 F7 09 B7 BD D5 F3 D6 B1 C1 F7 C4 B8 CF DF B5 E7 D1 B9 09 0A 23 09 31 09 23 31 09 31 32 38 2E 31 31 39 35 33 37 33 35 33 35 31 36 09 31 32 36 2E 30 34 38 37 31 33 36 38 34 30 38 32 09 33 37 2E 34 34 30 31 32 34 35 31 31 37 31 38 38 09 0A 23 09 32 09 23 32 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 0A 23 09 33 09 23 33 09 6E 75 6C 6C 09 6E 75 6C 6C 09 6E 75 6C 6C 09 0A 3C 2F 46 5A 3A 3A 47 5A 2E 5A 68 6F 6E 67 42 44 43 3E 0A 0A";
        byte[] bytes5 = ByteUtil.hexStringToBytes(fileContext4);
        System.out.println(bytes5.length);


        String lengthHex = " 60 05 00 00";
        byte[] bytes6 = ByteUtil.hexStringToBytes(lengthHex);
        int lengthInt = ByteUtil.byteArrayToInt(ByteUtil.reverse(bytes6));

        byte[] file = new byte[lengthInt];
        System.arraycopy(bytes1, 0, file, 0, bytes1.length);
        System.arraycopy(bytes2, 0, file, bytes1.length, bytes2.length);
        System.arraycopy(bytes3, 0, file, bytes1.length+ bytes2.length, bytes3.length);
        System.arraycopy(bytes4, 0, file, bytes1.length+ bytes2.length+bytes3.length, bytes4.length);
        System.arraycopy(bytes5, 0, file, bytes1.length+ bytes2.length+bytes3.length + bytes4.length, bytes5.length);
        System.out.println();
        File file1 = new File("C:\\Users\\DELL\\Desktop\\test.dat");

        FileOutputStream fileOutputStream = new FileOutputStream(file1);
        fileOutputStream.write(file);
        fileOutputStream.close();


    }

}

