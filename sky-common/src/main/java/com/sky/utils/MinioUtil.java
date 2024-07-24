package com.sky.utils;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;


@Data
@AllArgsConstructor
@Slf4j
public class MinioUtil {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;


    /**
     * 文件上传
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();

        try  {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(bytes), bytes.length, -1)
                            .build());

        } catch (MinioException oe) {
            System.out.println("Caught an MinioException, which means your request made it to Minio, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getMessage());
        } catch (Exception e) {
            System.out.println("Caught an Exception, which means the client encountered "
                    + "a serious internal problem while trying to communicate with MinIO, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + e.getMessage());
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        String url = null;
        try {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("生成的文件访问URL: {}", url);
        return url;
    }



}
