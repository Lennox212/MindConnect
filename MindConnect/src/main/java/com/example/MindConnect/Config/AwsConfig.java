package com.example.MindConnect.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration //tells Spring how to build objects that the rest of the application can use

public class AwsConfig {

    @Value("${aws.region}")
    private String region;


    @Bean
    public S3Client s3Client(){ //Performs operations against S3 (upload, download, delete, list objects,
        return S3Client.builder()
                .region(Region.of(region))
                .build();

    }

    @Bean
    public S3Presigner s3Presigner(){ //temporarily grant access to private S3 objects.
        return S3Presigner.builder()
                .region(Region.of(region))
                .build();
    }
}
