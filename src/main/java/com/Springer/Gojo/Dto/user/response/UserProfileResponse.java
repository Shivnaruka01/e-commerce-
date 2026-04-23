package com.Springer.Gojo.Dto.user.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileResponse(
		
		@Schema(description = "Full name of the authenticated user", example = "John Doe")
		String name, 
		
		@Schema(description = "Email address of the user", example = "gojo.satoru@example.com")
		String email
		) {}
