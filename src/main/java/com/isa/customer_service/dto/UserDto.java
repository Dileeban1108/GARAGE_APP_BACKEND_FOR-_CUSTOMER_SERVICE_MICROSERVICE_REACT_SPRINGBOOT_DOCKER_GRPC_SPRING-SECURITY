package com.isa.customer_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {
    private String accessToken;
    private String refreshToken;
    private String name;
    private String email;
    private String password;
    private String phoneNo;
    private String address;
   }
