package com.casatallermuso.backend.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.casatallermuso.backend.util.BucketValidator;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;


@Configuration
public class S3Config {

    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.accesskey}")
    private String accessKey;

    @Value("${s3.secretkey}")
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .endpointOverride(URI.create(endpoint))
            .region(Region.of(region))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            ))
            .serviceConfiguration(
                S3Configuration.builder()
                .pathStyleAccessEnabled(true)
                .build()
            )
            .build();
    }

    @Bean
    public BucketValidator bucketValidators(
        @Value("${s3.buckets.img}") String imagesBucketName
    ) {
        return new BucketValidator(imagesBucketName);
    }

}
