package com.Springer.Gojo.Dto.Exception;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // This hides "errors" if it's null
public record ErrorResponse (
		boolean success,
		String message,
		int status,
		Map<String, String> errors
){
	public ErrorResponse(boolean success, String message, int status) {
		this(success, message, status, null);
	}
}
