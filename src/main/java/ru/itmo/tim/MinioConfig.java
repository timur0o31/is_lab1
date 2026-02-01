package ru.itmo.tim;

import io.minio.MinioClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MinioConfig {

    private MinioClient minioClient;

    @PostConstruct
    void init() {
        System.out.println("minio_login=" + System.getProperty("minio_login"));
        System.out.println("minio_password=" + System.getProperty("minio_password"));
        this.minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials(System.getProperty("minio_login"), System.getProperty("minio_password"))
                .build();
    }

    public MinioClient getClient() {
        return minioClient;
    }
}
