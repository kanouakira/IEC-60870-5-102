package indi.kanouakira.iec102.util;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

/**
 * @author YDL
 * @ClassName: ByteUtil
 * @Description: byte 工具类
 * @date 2020年5月13日
 */
public class ByteUtil {

    /**
     * @param @param  i
     * @param @return
     * @return byte[]
     * @throws
     * @Title: intToByteArray
     * @Description: int 转换成 byte数组
     */
    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    //实现数组元素的翻转
    public static byte[] reverse(byte[] arr) {
        //遍历数组
        for (int i = 0; i < arr.length / 2; i++) {
            //交换元素 因为i从0开始所以这里一定要再减去1
            int temp = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = arr[i];
            arr[i] = (byte) temp;
        }
        //返回反转后的结果
        return arr;
    }

    /**
     * @param @param  val
     * @param @return
     * @return byte[]
     * @throws
     * @Title: shortToByteArray
     * @Description: short 转换成 byte[]
     */
    public static byte[] shortToByteArray(short val) {
        byte[] b = new byte[2];
        b[0] = (byte) ((val >> 8) & 0xff);
        b[1] = (byte) (val & 0xff);
        return b;
    }

    /**
     * @param @param  bytes
     * @param @return
     * @return int
     * @throws
     * @Title: byteArrayToInt
     * @Description: byte[] 转换成 int
     */
    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (3 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * @param @param  bytes
     * @param @return
     * @return short
     * @throws
     * @Title: byteArrayToShort
     * @Description: byte[] 转换成short
     */
    public static short byteArrayToShort(byte[] bytes) {
        short value = 0;
        for (int i = 0; i < 2; i++) {
            int shift = (1 - i) * 8;
            value += (bytes[i] & 0xFF) << shift;
        }
        return value;
    }

    /**
     * @param @param  date
     * @param @return
     * @return byte[]
     * @throws
     * @Title: date2HByte
     * @Description: 日期转换成 CP56Time2a
     */
    public static byte[] date2HByte(Date date) {
        ByteArrayOutputStream bOutput = new ByteArrayOutputStream();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 毫秒需要转换成两个字节其中 低位在前高位在后 
        // 先转换成short
        int millisecond = calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND);

        // 默认的高位在前
        byte[] millisecondByte = intToByteArray(millisecond);
        bOutput.write((byte) millisecondByte[3]);
        bOutput.write((byte) millisecondByte[2]);

        // 分钟 只占6个比特位 需要把前两位置为零 
        bOutput.write((byte) calendar.get(Calendar.MINUTE));
        // 小时需要把前三位置零
        bOutput.write((byte) calendar.get(Calendar.HOUR_OF_DAY));
        // 星期日的时候 week 是0 
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == Calendar.SUNDAY) {
            week = 7;
        } else {
            week--;
        }
        // 前三个字节是 星期 因此需要将星期向左移5位  后五个字节是日期  需要将两个数字相加 相加之前需要先将前三位置零
        bOutput.write((byte) (week << 5) + (calendar.get(Calendar.DAY_OF_MONTH)));
        // 前四字节置零
        bOutput.write((byte) ((byte) calendar.get(Calendar.MONTH) + 1));
        bOutput.write((byte) (calendar.get(Calendar.YEAR) - 2000));
        return bOutput.toByteArray();
    }


    /**
     * @param @param  date
     * @param @return
     * @return byte[]
     * @throws
     * @Title: date2HByte
     * @Description: CP56Time2a转换成时间
     */
    public static Date byte2HDate(byte[] dataByte) {
        int year = (dataByte[6] & 0x7F) + 2000;
        int month = dataByte[5] & 0x0F;
        int day = dataByte[4] & 0x1F;
        int hour = dataByte[3] & 0x1F;
        int minute = dataByte[2] & 0x3F;
        int second = dataByte[1] > 0 ? dataByte[1] : (int) (dataByte[1] & 0xff);
        int millisecond = dataByte[0] > 0 ? dataByte[0] : (int) (dataByte[0] & 0xff);
        millisecond = (second << 8) + millisecond;
        second = millisecond / 1000;
        millisecond = millisecond % 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    public static String byteArrayToHexString(byte[] array) {
        return byteArray2HexString(array, Integer.MAX_VALUE, false);
    }

    public static String byteArray2HexString(byte[] arrBytes, int count, boolean blank) {
        String ret = "";
        if (arrBytes == null || arrBytes.length < 1) {
            return ret;
        }
        if (count > arrBytes.length) {
            count = arrBytes.length;
        }
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            ret = Integer.toHexString(arrBytes[i] & 0xFF).toUpperCase();
            if (ret.length() == 1) {
                builder.append("0").append(ret);
            } else {
                builder.append(ret);
            }
            if (blank) {
                builder.append(" ");
            }
        }

        return builder.toString();

    }

    /**
     * 返回指定位置的数组
     *
     * @param bytes
     * @param start  开始位置
     * @param length 截取长度
     * @return
     */
    public static byte[] getByte(byte[] bytes, int start, int length) {
        byte[] ruleByte = new byte[length];
        int index = 0;
        while (index < length) {
            ruleByte[index++] = bytes[start++];
        }
        return ruleByte;
    }

    /**
     * 十六进制字符串转换成byte数组
     *
     * @param hexStr
     * @return
     */
    public static byte[] hexStringToBytes(String hexStr) {
        hexStr = hexStr.replaceAll(" ", "");
        hexStr = hexStr.toUpperCase();
        int len = (hexStr.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hexStr.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    // int转换为byte[4]数组
    public static byte[] getByteArray(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i & 0xff000000) >> 24);
        b[1] = (byte) ((i & 0x00ff0000) >> 16);
        b[2] = (byte) ((i & 0x0000ff00) >> 8);
        b[3] = (byte) (i & 0x000000ff);
        return b;
    }

    // 从byte数组的index处的连续4个字节获得一个int
    public static int getInt(byte[] arr, int index) {
        return (0xff000000 & (arr[index + 0] << 24)) |
                (0x00ff0000 & (arr[index + 1] << 16)) |
                (0x0000ff00 & (arr[index + 2] << 8)) |
                (0x000000ff & arr[index + 3]);
    }

    // float转换为byte[4]数组
    public static byte[] getByteArray(float f) {
        int intbits = Float.floatToIntBits(f);//将float里面的二进制串解释为int整数
        return getByteArray(intbits);
    }

    // 从byte数组的index处的连续4个字节获得一个float
    public static float getFloat(byte[] arr, int index) {
        return Float.intBitsToFloat(getInt(arr, index));
    }

    public static void main(String[] agrwu) {
        byte[] byteArray = getByteArray((float) 33.33);
        String s = ByteUtil.byteArrayToHexString(byteArray);
        System.out.println(byteArray);
    }
}
