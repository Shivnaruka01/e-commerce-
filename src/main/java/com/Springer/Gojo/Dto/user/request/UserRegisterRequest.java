package com.Springer.Gojo.Dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
		@NotBlank(message = "Name is required") 
		String name,

		@Email(message = "Invalid email found") 
		@NotBlank(message = "Name is required") 
		@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", 
		message = "Email format is invalid")
		String email,

		@NotBlank(message = "Password")
		@Size(min = 8, max = 20, message = "Password must be at least 6 characters") 
		@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", 
		message = "Password must contain at least one digit, one uppercase, one lowercase, and one special character")
		String password) {

}
