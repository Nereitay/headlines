package es.kiwi.wemedia.test;

import es.kiwi.common.aliyun.GreenImageScan;
import es.kiwi.common.aliyun.GreenTextScan;
import es.kiwi.file.service.FileStorageService;
import es.kiwi.wemedia.WemediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private GreenTextScan greenTextScan;

    @Autowired
    private GreenImageScan greenImageScan;

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testScanText() throws Exception {
        Map map = greenTextScan.greeTextScan("Hello world");
        System.out.println(map);
    }

    @Test
    public void testScanImage() throws Exception {
        byte[] bytes = fileStorageService.downLoadFile("http://192.168.1.150:9000/headlines/2023/09/13/98d43a29d6c147238a838c0d15d6e26e.jpg");
        List<byte[]> list = new ArrayList<>();
        Map map = greenImageScan.imageScan(list);
        System.out.println(map);
    }


}
