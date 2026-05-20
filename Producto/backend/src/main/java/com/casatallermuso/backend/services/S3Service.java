package com.casatallermuso.backend.services;

import org.springframework.web.multipart.MultipartFile;

import com.casatallermuso.backend.enums.TipoBucket;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;


public interface S3Service {

    public String cargarArchivo(MultipartFile archivo, TipoBucket tipoBucket);
    public ResponseInputStream<GetObjectResponse> descargarArchivo(String key, TipoBucket tipoBucket);

}
