package com.Springer.Gojo.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Springer.Gojo.Dto.user.request.UserLoginRequest;
import com.Springer.Gojo.Dto.user.request.UserRegisterRequest;
import com.Springer.Gojo.Dto.user.response.UserLoginResponse;
import com.Springer.Gojo.Dto.user.response.UserResponse;
import com.Springer.Gojo.entity.User;
import com.Springer.Gojo.exception.EmailAlreadyExistsException;
import com.Springer.Gojo.exception.InvalidCredentialsException;
import com.Springer.Gojo.exception.UserNotFoundException;
import com.Springer.Gojo.mapper.UserMapper;
import com.Springer.Gojo.repository.UserRepository;
import com.Springer.Gojo.security.JWTService;
import com.Springer.Gojo.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	private final JWTService jwtService;

	@Override
	public UserResponse register(UserRegisterRequest request) {

		userRepository.findByEmail(request.email()).ifPresent(user -> {
			throw new EmailAlreadyExistsException("Email already exist");
		});

//		String encodedPassword = passwordEncoder.encode(request.password());
//		
//		User user = UserMapper.toEntity(request, encodedPassword);

		User user = userMapper.toEntity(request, passwordEncoder.encode(request.password()));

		// Save to DB
//		User savedUser = userRepository.save(user);

		return userMapper.toResponse(userRepository.save(user));
	}

	// MapStruct handling it.

//	private UserResponse mapToRespone(User user) {
//		
//		return new UserResponse(
//				user.getId(),
//				user.getName(),
//				user.getEmail(),
//				user.getRole().name()				
//				);
//	}

	@Override
	public UserResponse getUserById(Long id) {

		return userRepository.findById(id).map(userMapper::toResponse)
				.orElseThrow(() -> new UserNotFoundException("user not found"));
	}

	@Override
	public UserLoginResponse login(UserLoginRequest request) {

		return userRepository.findByEmail(request.email())

				// Check if password matches
				.filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))

				// Convert the valid user to response dto
				.map(user -> {
					String token = jwtService.generateToken(user);
					return new UserLoginResponse(user.getId(), user.getEmail(), user.getRole(), token);
				})

				.orElseThrow(() -> new InvalidCredentialsException("Invalid email or passowrd"));

	}

}
