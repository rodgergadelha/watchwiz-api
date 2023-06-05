package com.example.movieappbackend.api.dtos.form;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterForm {
	
	@Email
	private String email;
	
	@NotBlank
	@Size(min = 6, max = 12)
	@Pattern(regexp = "^[a-zA-Z0-9]{6,12}$",
    message = "Username must be of 6 to 12 length with no special characters")
	private String username;
	
	@NotBlank
	@Size(min = 4, max = 12)
	@Pattern(regexp = "^[(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])]{4,12}$",
    message = "Password must contain atleast 1 uppercase, 1 lowercase, 1 special character and 1 digit")
	private String password;
	
	@Past
	@NotNull
	private Date birthdate;
}
