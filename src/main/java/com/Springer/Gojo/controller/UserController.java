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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/satoru/users")
@Validated
public class UserController {

	private final UserService userService;

	// Construction Injection
	public UserController(UserService userService) {
		this.userService = userService;
	}

	// Register User
	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegisterRequest request) {

		UserResponse response = userService.register(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

		UserResponse response = userService.getUserById(id);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<UserLoginResponse> login(@Valid @RequestBody UserLoginRequest request) {

		return ResponseEntity.ok(userService.login(request));
	}

	@GetMapping("/profile")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<String> getProfile() {
		return ResponseEntity.ok("If you see this, your JWT is VALID! Welcome, Satoru.");
	}

}
