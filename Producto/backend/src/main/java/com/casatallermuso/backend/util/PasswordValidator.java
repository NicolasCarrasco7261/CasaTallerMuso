package com.casatallermuso.backend.util;

import java.util.regex.Pattern;

public class PasswordValidator {

    private final String LET = "a-zA-Z";
    private final String NUM = "0-9";
    private final String SYM = "!@#$%&*()_+={}:;<>,.?~ \\-\\[\\]\\^";
    private final String VALIDATION_REGEX =
        "^(?=.*[" + LET + "])"              // al menos una letra
        + "(?=.*[" + NUM + "])"             // al menos un número
        + "(?=.*[" + SYM + "])"             // al menos un símbolo
        + "[" + LET + NUM + SYM + "]+$";    // solo carácteres permitidos

    private final int MIN_LENGTH = 8;
    private final int MAX_LENGTH = 64;

    private Pattern pattern;

    public PasswordValidator() {
        this.pattern = Pattern.compile(VALIDATION_REGEX);
    }

    public boolean validate(String password) {
        if (password == null) {
            return false;
        }

        if (password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            return false;
        }

        return pattern.matcher(password).matches();
    }
    
}
