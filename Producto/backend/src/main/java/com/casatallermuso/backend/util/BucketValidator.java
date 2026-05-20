package com.casatallermuso.backend.util;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.casatallermuso.backend.enums.TipoBucket;

import lombok.Getter;
import lombok.experimental.Accessors;

public class BucketValidator {

    private Map<TipoBucket, Bucket> buckets;

    public BucketValidator(String imagesBucketName) {
        this.buckets =  Map.ofEntries(
            Map.entry(
                TipoBucket.IMAGENES,
                new Bucket(
                    imagesBucketName,
                    542880L,  // 5mb
                    "^image/.*"
                )
            )
        );
    }

    public Bucket get(TipoBucket tipoBucket) {
        return buckets.get(tipoBucket);
    }

    @Accessors(fluent = true)
    public class Bucket {

        @Getter
        private String bucketName;

        @Getter
        private Long maxFileSize;

        private Pattern[] mimeRegexPatterns;

        public Bucket(String bucketName, Long maxFileSize, String... mimeRegexStrings) {
            this.bucketName = bucketName;
            this.maxFileSize = maxFileSize;
            this.mimeRegexPatterns = Arrays.stream(mimeRegexStrings)
                .map(str -> Pattern.compile(str))
                .toArray(Pattern[]::new);
        }

        public void validateFile(MultipartFile file) {
            if (file == null) {
                throw new IllegalArgumentException("Archivo no puede ser nulo");
            }

            validateMimeType(file.getContentType());
            validateFileSize(file.getSize());
        }

        private void validateMimeType(String mimeType) {
            if (mimeType == null) {
                throw new IllegalArgumentException("MIME type no puede ser null");
            }

            for (Pattern regex : this.mimeRegexPatterns) {
                if (regex.matcher(mimeType).matches()) return;
            }

            throw new IllegalArgumentException("MIME type inválido: " + mimeType);
        }

        private void validateFileSize(Long fileSize) {
            if (fileSize > this.maxFileSize) {
                throw new IllegalArgumentException("Archivo excede el límite de " + maxFileSize + " bytes (actual: " + fileSize + " bytes)");
            }
        }

    }

}
