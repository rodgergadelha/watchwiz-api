package com.example.movieappbackend.api.dtos.dto;

import java.time.OffsetDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

	private String uuid;
	private String username;
	private String email;
	private OffsetDateTime birthdate;
	private boolean enabled;
	private OffsetDateTime registrationDate;
}
