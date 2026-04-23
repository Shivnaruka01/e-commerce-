package com.Springer.Gojo.Dto.user.response;

import com.Springer.Gojo.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserLoginResponse(

		@Schema(description = "Primary key of the user", example = "101")
		Long userId, 
		
		@Schema(example = "gojo.satoru@example.com")
		String email, 
		
		@Schema(description = "Assigned user privileges", example = "ADMIN")
		Role role, 
		
		@Schema(description = "JWT Bearer token for authenticated requests")
		String token
		) {}
