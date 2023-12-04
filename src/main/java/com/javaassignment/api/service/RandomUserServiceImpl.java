package com.javaassignment.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaassignment.api.model.User;
import com.javaassignment.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RandomUserServiceImpl implements RandomUserService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private User savedUser;
    private List<String> nationalities;

    private String savedFirstName; // Variable to store the name


    @Autowired
    public RandomUserServiceImpl(
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper,
            UserRepository userRepository) {
        this.webClient = webClientBuilder.baseUrl("https://randomuser.me").build();
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Override
    public String getRandomUser() {
        // Make a request to the first API
        String jsonResult = webClient.get()
                .uri("/api/")
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Note: blocking for simplicity, consider using reactive programming

        // Save the name obtained from the first API
        savedFirstName = parseFirstName(jsonResult);
        savedUser = parseUser(jsonResult);
        User user = parseUser(jsonResult);
        if (user != null) {
            userRepository.save(user);
        }

        return jsonResult;
    }

    @Override
    public String getGender() {
        try {
            if (savedFirstName != null) {
                // Encode the name to handle special characters
                String encodedFirstName = URLEncoder.encode(savedFirstName, StandardCharsets.UTF_8.toString());

                // Use the encoded name in the URL of the third API
                String thirdApiUrl = "https://api.genderize.io/?name=" + encodedFirstName;

                // Make a request to the third API using WebClient
                String thirdApiResponse = webClient.get()
                        .uri(thirdApiUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block(); // Note: blocking for simplicity, consider using reactive programming

                return thirdApiResponse;
            } else {
                // Handle the case where the first name is not available
                return "Error: First name not available. Call getRandomUser first.";
            }
        } catch (UnsupportedEncodingException e) {
            // Handle the encoding exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getNationality() {
        try {
            if (savedFirstName != null) {
                // Encode the name to handle special characters
                String encodedFirstName = URLEncoder.encode(savedFirstName, StandardCharsets.UTF_8.toString());

                // Use the encoded name in the URL of the third API
                String secondApiUrl = "https://api.nationalize.io/?name=" + encodedFirstName;

                // Make a request to the third API using WebClient
                String secondApiResponse = webClient.get()
                        .uri(secondApiUrl)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block(); // Note: blocking for simplicity, consider using reactive programming

                nationalities = parseNationalities(secondApiResponse);

                return secondApiResponse;
            } else {
                // Handle the case where the first name is not available
                return "Error: First name not available. Call getRandomUser first.";
            }
        } catch (UnsupportedEncodingException e) {
            // Handle the encoding exception
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String verifyUser(User user) {
        if (user != null) {
            // Perform verification based on the user data
            // Example logic: If the user's nationality is in the list of nationalities from the second API response
            if (user.getNationality() != null && nationalities.contains(user.getNationality())) {
                user.setVerificationResult("VERIFIED");
            } else {
                user.setVerificationResult("TO_BE_VERIFIED");
            }
            return user.getVerificationResult();
        } else {
            // Handle the case where user information is not available
            return "Error: User information not available. Call getRandomUser first.";
        }
    }

    @Override
    public User getParsedUser(String jsonResult) {
        return parseUser(jsonResult);
    }

    private List<String> parseNationalities(String jsonResult) {
        try {
            // Implement this method to parse the list of nationalities from the second API response
            // Return a list of nationalities
            // Example: ["IN", "US", "UK"]

            List<String> nationalities = new ArrayList<>();
            JsonNode rootNode = objectMapper.readTree(jsonResult);

            // Assuming there is a "country" node containing an array of countries
            JsonNode countryNode = rootNode.path("country");

            for (JsonNode entry : countryNode) {
                String countryId = entry.path("country_id").asText();
                nationalities.add(countryId);
            }

            return nationalities;
        } catch (Exception e) {
            // Handle the exception (e.g., log it or throw a custom exception)
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



    private String parseFirstName(String jsonResult) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResult);
            JsonNode firstNode = rootNode.path("results").path(0).path("name").path("first");
            return firstNode.asText();
        } catch (Exception e) {
            // Handle the exception (e.g., log it or throw a custom exception)
            e.printStackTrace();
            return null;
        }
    }

    private User parseUser(String jsonResult) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResult);
            JsonNode resultsNode = rootNode.path("results").path(0);

            String firstName = resultsNode.path("name").path("first").asText();
            String nationality = resultsNode.path("nat").asText();
            String gender = resultsNode.path("gender").asText();
            int age = resultsNode.path("dob").path("age").asInt();
            String dob = resultsNode.path("dob").path("date").asText();
            String verificationResult = "TO_BE_VERIFIED";

            return new User(firstName, nationality, gender, age, dob, verificationResult);
        } catch (Exception e) {
            // Handle the exception (e.g., log it or throw a custom exception)
            e.printStackTrace();
            return null;
        }
    }
}
