package com.javaassignment.api.service;

import com.javaassignment.api.model.User;

public interface RandomUserService {
    String getRandomUser();

    String getNationality();

    String getGender();

    String verifyUser(User user);

    User getParsedUser(String jsonResult);
}
