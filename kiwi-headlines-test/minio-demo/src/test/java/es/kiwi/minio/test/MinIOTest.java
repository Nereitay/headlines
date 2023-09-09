package es.kiwi.minio.test;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinIOTest {

    /**
     * 把list.html文件上传到minio中， 并且可以在浏览器中访问
     */
    @Test
    public void test() {

        try(FileInputStream fileInputStream =
                    new FileInputStream("D:\\ideawork\\list.html")) {

            // 1.获取minio的链接信息 创建一个minio的客户端
            MinioClient minioClient = MinioClient.builder()
                    .credentials("minio", "minio123")
                    .endpoint("http://192.168.1.150:9000").build();

            // 2.上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object("list.html") // 文件名称
                    .contentType("text/html") // 文件类型
                    .bucket("headlines") // 桶名称
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);

            // 访问路径
            System.out.println("http://192.168.1.150:9000/headlines/list.html");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
