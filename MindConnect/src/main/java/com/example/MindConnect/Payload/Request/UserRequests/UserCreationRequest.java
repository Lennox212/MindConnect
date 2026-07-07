package com.example.MindConnect.Payload.Request.UserRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class UserCreationRequest {

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private String gender;

    private String mentalCondition;


}
