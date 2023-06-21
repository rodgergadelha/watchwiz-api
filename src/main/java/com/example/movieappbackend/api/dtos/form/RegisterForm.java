package com.example.movieappbackend.api.dtos.form;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RegisterForm {
	
	@Email(message = "Must enter a valid email")
	@NotBlank
	private String email;
	
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9]{3,25}$",
    message = "Username must be of 3 to 25 length with no special characters")
	private String username;
	
	@NotBlank
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{6,}$",
    message = "Password must contain at least 1 uppercase, 1 lowercase, 1 special character and 1 digit, and a size of at least 6 characters")
	private String password;
	
	@Past
	@NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Schema(description = "format: dd/MM/yyyy")
	private LocalDate birthdate;
	
	private MultipartFile image;
}
