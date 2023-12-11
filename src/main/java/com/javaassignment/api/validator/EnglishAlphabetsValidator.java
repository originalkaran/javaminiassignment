package com.javaassignment.api.validator;

public class EnglishAlphabetsValidator implements Validator<String> {

    @Override
    public boolean validate(String input) {
        // Implement English alphabets validation logic
        return input != null && input.matches("^[a-zA-Z]*$");
    }
}
