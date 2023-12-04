package com.javaassignment.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaassignment.api.model.User;
import com.javaassignment.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RandomUserServiceImpl implements RandomUserService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private User savedUser;
    private List<String> nationalities;
    private String gender;

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
    public String createUser(int numberOfUsers) {
        try {
            // Check if numberOfUsers is within the allowed range (1 to 5)
            if (numberOfUsers < 1 || numberOfUsers > 5) {
                throw new IllegalArgumentException("size must be between 1 and 5.");
            }

            List<String> userResponses = new ArrayList<>();

            for (int i = 0; i < numberOfUsers; i++) {
                // Call the getRandomUser method from the service to get the user information
                String randomUserResponse = getRandomUser();

                // Parse the user information to get the required parameters
                User user = getParsedUser(randomUserResponse);

                // Call the necessary methods to get gender and nationality
                getGender();
                getNationality();


            // Perform verification based on the user data
            // Example logic: If the user's nationality is in the list of nationalities
            // from the second API response and user's gender matches the gender from the third API response
            if (user.getNationality() != null && nationalities.contains(user.getNationality()) && user.getGender().equals(gender)) {
                user.setVerificationResult("VERIFIED");
            } else {
                user.setVerificationResult("TO_BE_VERIFIED");
            }

            // Save the user to the database
            userRepository.save(user);

            // Log or print verification result for debugging
            System.out.println("Verification Result: " + user.getVerificationResult());
                userResponses.add(formatUserResponse(user));
            }

            // Return the formatted response as a JSON array
            return "[" + String.join(",", userResponses) + "]";
        } catch (IllegalArgumentException e) {
            // Handle the case where numberOfUsers is outside the allowed range
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            // Handle any other exceptions during the process
            e.printStackTrace();
            return "Error during user creation.";
        }
    }

    private String formatUserResponse(User user) {
        // Format the user information for the response
        return String.format("{\"name\": \"%s\", \"dob\": \"%s\", \"gender\": \"%s\", \"nationality\": \"%s\", \"verification_status\": \"%s\"}",
                user.getFirstName(), user.getDob(), user.getGender(), user.getNationality(), user.getVerificationResult());
    }

    @Override
    public String getRecentUsers(int limit, int offset) {
        try {

            if (limit < 1 || limit > 5) {
                throw new IllegalArgumentException("size must be between 1 and 5.");
            }
            if (offset < 0) {
                throw new IllegalArgumentException("Offset must be non-negative.");
            }

            Pageable pageable = PageRequest.of(offset, limit);
            List<User> recentUsers = userRepository.findTopNOrderedByIdDesc(pageable);

            // Format the list of users for the response
            return formatRecentUsersResponse(recentUsers);
        } catch (Exception e) {
            // Handle any exceptions during the process
            e.printStackTrace();
            return "Error fetching recent users.";
        }
    }

    private String formatRecentUsersResponse(List<User> users) throws JsonProcessingException {
        // Create a list to store formatted user responses
        List<String> userResponses = new ArrayList<>();

        // Iterate through the list of users and format each user individually
        for (User user : users) {
            userResponses.add(formatUserResponsedb(user));
        }

        // Return the formatted user responses as a JSON array
        return "[" + String.join(",", userResponses) + "]";
    }

    private String formatUserResponsedb(User user) throws JsonProcessingException {
        // Format an individual user for the response
        return objectMapper.writeValueAsString(user);
    }



    @Override
    public String getRandomUser() {
        // Make a request to the first API
        String jsonResult = webClient.get()
                .uri("/api/")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Save the name obtained from the first API
        savedFirstName = parseFirstName(jsonResult);
        savedUser = parseUser(jsonResult);
//        if (savedUser != null) {
//            userRepository.save(savedUser);
//        }

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
                        .block();

                gender = parseGender(thirdApiResponse);
                System.out.println("Gender: " + gender);

                return thirdApiResponse;
            } else {
                return "Error: First name not available. Call getRandomUser first.";
            }
        } catch (UnsupportedEncodingException e) {
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
                        .block();

                nationalities = parseNationalities(secondApiResponse);
                System.out.println("Nationalities: " + nationalities);

                return secondApiResponse;
            } else {
                return "Error: First name not available. Call getRandomUser first.";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String verifyUser() {
        try {
            if (savedUser != null) {
                // Log or print user information for debugging
                System.out.println("User Information: " + savedUser.toString());

                // Log or print gender and nationalities for debugging
                System.out.println("Gender: " + gender);
                System.out.println("Nationalities: " + nationalities);

                // Perform verification based on the user data
                if (savedUser.getNationality() != null && nationalities.contains(savedUser.getNationality()) && savedUser.getGender().equals(gender)) {
                    savedUser.setVerificationResult("VERIFIED");
                } else {
                    savedUser.setVerificationResult("TO_BE_VERIFIED");
                }

                // Log or print verification result for debugging
                System.out.println("Verification Result: " + savedUser.getVerificationResult());

                // Save the updated user to the database
                userRepository.save(savedUser);

                return savedUser.getVerificationResult();
            } else {
                return "Error: User information is null. Call getRandomUser first.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during verification.";
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

    private String parseGender(String jsonResult){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResult);

            // Access the "gender" field
            String gender = rootNode.path("gender").asText();
            return gender;
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
            String verificationResult = "LULU";

            return new User(firstName, nationality, gender, age, dob, verificationResult);
        } catch (Exception e) {
            // Handle the exception (e.g., log it or throw a custom exception)
            e.printStackTrace();
            return null;
        }
    }
}
