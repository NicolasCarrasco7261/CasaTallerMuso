package com.casatallermuso.backend.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.casatallermuso.backend.enums.TipoBucket;
import com.casatallermuso.backend.util.BucketValidator;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;
    private final BucketValidator bucketValidator;

    @Override
    public String cargarArchivo(MultipartFile file, TipoBucket tipoBucket) {
        try {
            var bucket = bucketValidator.get(tipoBucket);
            // bucket.validateFile(file);

            String key = UUID.randomUUID().toString();

            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket.bucketName())
                .key(key)
                .contentType(file.getContentType())
                .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return key;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseInputStream<GetObjectResponse> descargarArchivo(String key, TipoBucket tipoBucket) {
        try {
            var bucket = bucketValidator.get(tipoBucket);

            GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket.bucketName())
                .key(key)
                .build();
            
            var responseFile = s3Client.getObject(request);
            return responseFile;
        } catch (S3Exception e) {
            throw new ResponseStatusException(HttpStatus.resolve(e.statusCode()));
        }
    }

}
