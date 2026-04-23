package com.Springer.Gojo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Springer.Gojo.Dto.user.request.UserLoginRequest;
import com.Springer.Gojo.Dto.user.request.UserRegisterRequest;
import com.Springer.Gojo.Dto.user.response.UserLoginResponse;
import com.Springer.Gojo.Dto.user.response.UserResponse;
import com.Springer.Gojo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/satoru/users")
@Validated
@Tag(name = "User Controller", description = "Operations related to User Registeration and Authentication")
public class UserController {

	private final UserService userService;

	// Construction Injection
	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Register User
	@Operation(summary = "Register a new user", description = "Creates a new user account and returns user details")
	@ApiResponse(responseCode = "201", description = "User created successfully")
	@ApiResponse(responseCode = "409", description = "Email already exists")
	@PostMapping("/register")
	@SecurityRequirements // No lock icon will show up for Register either
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {

		UserResponse response = userService.register(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@Operation(summary = "Fetch user data", description = "Get user data by user id")
	@GetMapping("{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

		UserResponse response = userService.getUserById(id);

		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Login user", description = "Authenticate user and return a JWT token")
	@PostMapping("/login")
	@SecurityRequirements  // This "blanks out" the global security for this specific method
	public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {

		return ResponseEntity.ok(userService.login(request));
	}

	@GetMapping("/profile")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<String> getProfile() {
		return ResponseEntity.ok("If you see this, your JWT is VALID! Welcome, Satoru.");
	}

}
