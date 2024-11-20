package com.customer.care.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotEmpty(message = "Email Required*")
    private String email;

    @NotEmpty(message = "Full Name Required*")
    private String fullName;

    @NotEmpty
    private String phone;

    @Size(min = 6,message = "Minimum password length is 6 characters")
    private String password;

}
