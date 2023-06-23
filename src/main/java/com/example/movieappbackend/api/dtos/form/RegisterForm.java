package com.example.movieappbackend.api.dtos.form;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

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
	@Size(min = 3, max = 25, message = "username must have between 3 and 25 characters")
	private String username;
	
	@NotBlank
	@Size(min = 6, message = "password must have at least 6 characters")
	private String password;
	
	@Past
	@NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Schema(description = "format: dd/MM/yyyy")
	private LocalDate birthdate;
	
	private MultipartFile image;
}
