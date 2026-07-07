package com.example.MindConnect.Payload.Response.UserResponses;


import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String firstName;

    private String lastName;

    private String email;

    private String token;

    private String message;

    private LocalDateTime localDateTime;

}
