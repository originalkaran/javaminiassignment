package com.javaassignment.api.validator;

public class ValidatorFactory {
    public static Validator getValidator(String input) {
        if (isNumeric(input)) {
            return new NumericValidator();
        } else {
            return new EnglishAlphabetsValidator();
        }
    }

    private static boolean isNumeric(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

