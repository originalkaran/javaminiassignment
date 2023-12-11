package com.javaassignment.api.controller;

import com.javaassignment.api.model.User;
import com.javaassignment.api.service.RandomUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RandomUserController {

    private final RandomUserService randomUserService;

    public RandomUserController(RandomUserService randomUserService) {
        this.randomUserService = randomUserService;
    }

    @GetMapping("/randomuser")
    public ResponseEntity<String> getRandomUser() {
        String randomUserResponse = randomUserService.getRandomUser();

        // Modify this part based on your requirements
        if (randomUserResponse != null) {
            return ResponseEntity.ok(randomUserResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @GetMapping("/nationality")
    public ResponseEntity<String> getNationality() {
        // Call the second API (nationality) with the provided name
        String nationalityResponse = randomUserService.getNationality();

        // Modify this part based on your requirements
        if (nationalityResponse != null) {
            return ResponseEntity.ok(nationalityResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @GetMapping("/gender")
    public ResponseEntity<String> getGender() {
        // Call the third API (gender) with the provided name
        String genderResponse = randomUserService.getGender();

        // Modify this part based on your requirements
        if (genderResponse != null) {
            return ResponseEntity.ok(genderResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @GetMapping("/verifyuser")
    public ResponseEntity<String> verifyUser() {
        // Call the verifyUser method from the service to get the verification result
        String verificationResult = randomUserService.verifyUser();

        // Modify this part based on your requirements
        if (verificationResult != null) {
            return ResponseEntity.ok(verificationResult);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }


    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestParam(defaultValue = "1") int size) {
        // Call the createUser method from the service to create and verify the user
        String userCreationResponse = randomUserService.createUser(size);

        // Modify this part based on your requirements
        if (userCreationResponse != null) {
            return ResponseEntity.ok(userCreationResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @GetMapping("/recentusers")
    public ResponseEntity<String> getRecentUsers(
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "sortType", defaultValue = "Name") String sortType,
            @RequestParam(name = "sortOrder", defaultValue = "Odd") String sortOrder) {
        String recentUsersResponse = randomUserService.getRecentUsers(limit, offset, sortType, sortOrder);

        // Modify this part based on your requirements
        if (recentUsersResponse != null) {
            return ResponseEntity.ok(recentUsersResponse);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }
}
