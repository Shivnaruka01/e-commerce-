package com.Springer.Gojo.Dto.Exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL) // This hides "errors" if it's null
public record ErrorResponse (
		@Schema(example = "false")boolean success,
		@Schema(example = "Validation Failed")String message,
		@Schema(example = "400")int status,
		@Schema(example = "{\"email\": \"Invalid email format\"}")Map<String, String> errors
){
	public ErrorResponse(boolean success, String message, int status) {
		this(success, message, status, null);
	}
}
