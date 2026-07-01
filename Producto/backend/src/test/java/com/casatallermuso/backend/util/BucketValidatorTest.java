package com.casatallermuso.backend.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import com.casatallermuso.backend.enums.TipoBucket;

public class BucketValidatorTest {

    @Test
    @DisplayName("get: debe retornar configuracion del bucket de imagenes")
    void get_imagenes() {
        BucketValidator validator = new BucketValidator("imagenes-test");

        var bucket = validator.get(TipoBucket.IMAGENES);

        assertThat(bucket.bucketName()).isEqualTo("imagenes-test");
        assertThat(bucket.maxFileSize()).isEqualTo(542880L);
    }

    @Test
    @DisplayName("validateFile: debe aceptar imagen dentro del limite")
    void validateFile_ok() {
        var bucket = new BucketValidator("imagenes-test").get(TipoBucket.IMAGENES);
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "foto.png",
            "image/png",
            new byte[] { 1, 2, 3 }
        );

        assertDoesNotThrow(() -> bucket.validateFile(file));
    }

    @Test
    @DisplayName("validateFile: debe rechazar archivo nulo")
    void validateFile_nullFile() {
        var bucket = new BucketValidator("imagenes-test").get(TipoBucket.IMAGENES);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bucket.validateFile(null)
        );

        assertThat(exception.getMessage()).contains("no puede ser nulo");
    }

    @Test
    @DisplayName("validateFile: debe rechazar MIME type nulo")
    void validateFile_nullMimeType() {
        var bucket = new BucketValidator("imagenes-test").get(TipoBucket.IMAGENES);
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "foto.png",
            null,
            new byte[] { 1, 2, 3 }
        );

        assertThrows(IllegalArgumentException.class, () -> bucket.validateFile(file));
    }

    @Test
    @DisplayName("validateFile: debe rechazar MIME type no permitido")
    void validateFile_invalidMimeType() {
        var bucket = new BucketValidator("imagenes-test").get(TipoBucket.IMAGENES);
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "documento.pdf",
            "application/pdf",
            new byte[] { 1, 2, 3 }
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bucket.validateFile(file)
        );

        assertThat(exception.getMessage()).contains("MIME type");
    }

    @Test
    @DisplayName("validateFile: debe rechazar archivos sobre el limite")
    void validateFile_tooLarge() {
        var bucket = new BucketValidator("imagenes-test").get(TipoBucket.IMAGENES);
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "foto.png",
            "image/png",
            new byte[542881]
        );

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bucket.validateFile(file)
        );

        assertThat(exception.getMessage()).contains("excede");
    }
}
