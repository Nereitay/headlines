package es.kiwi.minio.test;

import es.kiwi.MinIOApplication;
import es.kiwi.file.service.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest(classes = MinIOApplication.class)
@RunWith(SpringRunner.class)
public class MinIOSpringbootTest {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void test() {

        try (InputStream inputStream = Files.newInputStream(Paths.get("D:\\ideawork\\list.html"))) {
            String path = fileStorageService.uploadHtmlFile("", "list.html", inputStream);
            System.out.println(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
