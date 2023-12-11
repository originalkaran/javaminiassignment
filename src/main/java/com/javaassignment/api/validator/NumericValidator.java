package com.javaassignment.api.validator;

public class NumericValidator implements Validator<Integer> {

    @Override
    public boolean validate(Integer input) {
        // Implement numeric validation logic
        return input != null && input >= 0;
    }
}
