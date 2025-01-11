package com.subscriptiontracker.DTO;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String email;
    private String password;
    private String passwordConfirmation;
    private String firstName;
    private String lastName;
}
