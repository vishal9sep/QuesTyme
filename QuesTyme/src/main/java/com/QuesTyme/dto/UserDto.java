package com.QuesTyme.dto;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

	private int id;

	@NotEmpty
	@Size(min = 4, message = "Username must be min of 4 characters !!")
	private String name;

	@NotEmpty(message = "Email is required !!")
	@Email(message = "Email address is not valid")
	private String email;
	@NotEmpty
	@Size(min = 3, max = 8, message = "password must be min of 3 chars and max of 8 chars !!")
	private String password;

	private Set<RoleDto> roles = new HashSet<>();

	public UserDto(String name, String s, String s1) {
		this.name=name;
		this.email=s;
		this.password=s1;
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}
}
