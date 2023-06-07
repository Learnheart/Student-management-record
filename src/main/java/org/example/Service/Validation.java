package org.example.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static Pattern pattern;
    private Matcher matcher;
    private static final String NAME_REGEX =   "^[a-z A-Z]{1,50}$";
    private static final String EMAIL_REGEX =   "^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)$";
    private static final String NUMBER_REGEX = "^[0-9]+$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{8,15}$";

    public boolean validateEmail(String regex) {
        pattern = Pattern.compile(EMAIL_REGEX);
        matcher = pattern.matcher(regex);
        return matcher.matches();
    }

    public boolean validateName(String regex) {
        pattern = Pattern.compile(NAME_REGEX);
        matcher = pattern.matcher(regex);
        return matcher.matches();
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public boolean validateGender(String input) {
        try {
            Gender.valueOf(input.toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean validateNumber(String regex) {
        pattern = Pattern.compile(NUMBER_REGEX);
        matcher = pattern.matcher(regex);
        return matcher.matches();
    }

    public boolean validatePhone(String regex) {
        pattern = Pattern.compile(PHONE_REGEX);
        matcher = pattern.matcher(regex);
        return matcher.matches();
    }

}
