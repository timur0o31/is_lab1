package ru.itmo.tim;

import io.minio.MinioClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class    MinioConfig {

    private MinioClient minioClient;

    @PostConstruct
    void init() {
        System.out.println("minio_login=" + System.getenv("minio_login"));
        System.out.println("minio_password=" + System.getenv("minio_password"));
        this.minioClient = MinioClient.builder()
                .endpoint(System.getenv("minio_endpoint"))
                .credentials(System.getenv("minio_login"), System.getenv("minio_password"))
                .build();
    }

    public MinioClient getClient() {
        return minioClient;
    }
}
