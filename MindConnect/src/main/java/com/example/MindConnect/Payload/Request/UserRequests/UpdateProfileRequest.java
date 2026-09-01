package com.example.MindConnect.Payload.Request.UserRequests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UpdateProfileRequest {
    private String firstName;

    private String lastName;

    private String gender;

    private String mentalCondition;

    private String bio;
}
