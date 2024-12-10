package com.isa.customer_service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class VerifyUserDto {
    private String email;
    private String verificationCode;
    private String password;

}
