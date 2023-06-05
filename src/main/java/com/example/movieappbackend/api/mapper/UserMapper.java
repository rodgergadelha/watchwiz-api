package com.example.movieappbackend.api.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.movieappbackend.api.dtos.dto.UserDto;
import com.example.movieappbackend.api.dtos.form.RegisterForm;
import com.example.movieappbackend.domain.model.User;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserMapper {
	
	private final ModelMapper modelMapper;
	
	public User formToEntity(RegisterForm form) {
		return modelMapper.map(form, User.class);
	}
	
	public UserDto entityToDto(User user) {
		return modelMapper.map(user, UserDto.class);
	}
	
	public void copyFormDataToEntity(RegisterForm form, User user) {
		modelMapper.map(form, user);
	}
}
