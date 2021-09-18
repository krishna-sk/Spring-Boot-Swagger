package com.krishna.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

	private Integer id;

	@Size(min = 2, message = "first name cannot be less than 2 characters")
	@NotBlank
	private String firstName;

	@Size(min = 2, message = "last name cannot be less than 2 characters")
	@NotBlank
	private String lastName;

	@Size(min = 4, message = "role cannot be less than 4 characters")
	@NotBlank
	private String role;

	@Email(message = "Enter a valid Email")
	@NotBlank
	private String email;
	
	private String token;

}
