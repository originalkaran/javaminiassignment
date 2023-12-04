package com.javaassignment.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    private String firstName;
    private String nationality;
    private String gender;
    private int age;
    private String dob;
    private String verificationResult;

    public User(String firstName, String nationality, String gender, int age, String dob, String verificationResult){
        this.firstName = firstName;
        this.nationality = nationality;
        this.gender = gender;
        this.age = age;
        this.dob = dob;
        this.verificationResult = "TO_BE_VERIFIED";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getVerificationResult() {
        return verificationResult;
    }

    public void setVerificationResult(String verificationResult) {
        this.verificationResult = verificationResult;
    }
}
