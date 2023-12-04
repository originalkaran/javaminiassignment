package com.javaassignment.api.controller;

import com.javaassignment.api.model.User;
import com.javaassignment.api.service.RandomUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        // Call the getRandomUser method from the service to get the user information
        String randomUserResponse = randomUserService.getRandomUser();

        // Parse the user information to get the required parameters
        User user = randomUserService.getParsedUser(randomUserResponse);

        // Call the verifyUser method from the service to get the verification result
        String verificationResult = randomUserService.verifyUser(user);

        // Modify this part based on your requirements
        if (verificationResult != null) {
            return ResponseEntity.ok(verificationResult);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }
}
