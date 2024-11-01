package com.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForm {

    @NotBlank(message="Username can not be empty")
    @Size(min=3,message="Username must be of at least 3 characters")
    private String name;
    @Email(message="Invalid email address")
    @NotBlank(message="Email is required")
    private String email;
    @NotBlank(message="Password can not be empty")
    @Size(min=8,message="Password must be at least 8 characters")
    private String password;
    @NotBlank(message="Description is required")
    private String about;
    @Size(min=8,max=12,message="Invalid number")
    private String phoneNumber;

}
