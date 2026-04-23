package com.Springer.Gojo.Dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(

	    @Schema(description = "Registered user email address", example = "gojo.satoru@example.com")
		@Email(message = "Invalid email format")
		@NotBlank(message = "Email is required") 
		String email,

	    @Schema(description = "User account password", example = "SecurePass123!")
		@NotBlank(message = "Password is required") 
	    String password
	    ) {}
