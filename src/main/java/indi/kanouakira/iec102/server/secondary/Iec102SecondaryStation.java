package indi.kanouakira.iec102.server.secondary;

import indi.kanouakira.iec102.core.Iec102DataConfig;
import indi.kanouakira.iec102.core.Iec102SecondaryAbstractFactory;
import indi.kanouakira.iec102.core.Iec102UploadFile;
import indi.kanouakira.iec102.core.enums.TypeIdentificationEnum;

import java.io.File;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;

/**
 * @author KanouAkira
 * @date 2022/4/18 20:28
 */
public class Iec102SecondaryStation extends SecondaryStation {
    public Iec102SecondaryStation(int port) {
        super(port, new Iec102SecondaryAbstractFactory());
    }

    public static void main(String[] args) throws Exception {

        byte[] fileContext = Files.readAllBytes(new File("C:\\Users\\DELL\\Desktop\\GF_GZ.ZhongBDC_XB-FZ_20211115_101500.dat").toPath());
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        instance.add(Calendar.MINUTE,  5);
        // 构造一个文件，传入作为测试，启动5分钟后过期。
        Iec102UploadFile iec102UploadFile = new Iec102UploadFile(TypeIdentificationEnum.ZTXX_QXHJ, "GF_GZ.ZhongBDC_XB-FZ_20211115_101500.dat", fileContext, instance.getTime());
        Iec102DataConfig.addFile(iec102UploadFile);

        new Iec102SecondaryStation(3000).run() ;
    }
}