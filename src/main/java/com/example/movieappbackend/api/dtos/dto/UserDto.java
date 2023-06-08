package com.example.movieappbackend.api.dtos.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

	private String uuid;
	private String username;
	private String email;
	private String birthdate;
	private boolean enabled;
	private String registrationDate;
	private String profileImagePath;
}
