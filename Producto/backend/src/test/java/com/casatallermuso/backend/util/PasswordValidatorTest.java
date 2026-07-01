package com.casatallermuso.backend.util;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    @DisplayName("validate: debe aceptar claves validas")
    void validate_validPassword() {
        assertThat(validator.validate("Clave123")).isTrue();
        assertThat(validator.validate("Clave123!")).isTrue();
        assertThat(validator.validate("Abcdefghijklmnopqrstuvwxyz1234567890")).isTrue();
    }

    @Test
    @DisplayName("validate: debe rechazar null")
    void validate_null() {
        assertThat(validator.validate(null)).isFalse();
    }

    @Test
    @DisplayName("validate: debe rechazar claves fuera de largo permitido")
    void validate_length() {
        assertThat(validator.validate("A1short")).isFalse();
        assertThat(validator.validate("A1" + "x".repeat(63))).isFalse();
    }

    @Test
    @DisplayName("validate: debe exigir letras y numeros")
    void validate_requiresLettersAndNumbers() {
        assertThat(validator.validate("12345678")).isFalse();
        assertThat(validator.validate("SoloLetras")).isFalse();
    }

    @Test
    @DisplayName("validate: debe rechazar caracteres no permitidos")
    void validate_invalidCharacters() {
        assertThat(validator.validate("Clave123/")).isFalse();
        assertThat(validator.validate("Clave123á")).isFalse();
    }
}
