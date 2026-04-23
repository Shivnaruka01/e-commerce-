package com.Springer.Gojo.Dto.user.response;

import com.Springer.Gojo.entity.Role;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserResponse(
	    @Schema(description = "Unique database ID of the user", example = "101")
	    Long id, 

	    @Schema(description = "Full name", example = "John Doe")
	    String name, 

	    @Schema(description = "Contact email", example = "john.doe@example.com")
	    String email, 

	    @Schema(description = "Assigned user role", example = "USER")
	    Role role
	) {}

