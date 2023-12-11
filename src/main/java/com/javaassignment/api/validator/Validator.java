package com.javaassignment.api.validator;

public interface Validator<T> {
    boolean validate(T input);
}
