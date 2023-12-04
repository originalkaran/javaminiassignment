package com.javaassignment.api.service;

import com.javaassignment.api.dto.CreateUserRequest;
import com.javaassignment.api.model.User;

import java.util.List;

public interface RandomUserService {
    String createUser(int size);

    String getRecentUsers(int limit, int offset);

    String getRandomUser();

    String getNationality();

    String getGender();

    String verifyUser();

    User getParsedUser(String jsonResult);

}
