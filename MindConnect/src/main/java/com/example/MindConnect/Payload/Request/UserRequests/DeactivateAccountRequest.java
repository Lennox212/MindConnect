package com.example.MindConnect.Payload.Request.UserRequests;

import com.example.MindConnect.Entity.BaseClass;
import com.example.MindConnect.Enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class DeactivateAccountRequest extends BaseClass {


    private String reason;

    AccountStatus accountStatus;

    private String passwordConfirmation;

}
