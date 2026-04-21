package com.Springer.Gojo.service;

import com.Springer.Gojo.Dto.user.request.UserLoginRequest;
import com.Springer.Gojo.Dto.user.request.UserRegisterRequest;
import com.Springer.Gojo.Dto.user.response.UserLoginResponse;
import com.Springer.Gojo.Dto.user.response.UserResponse;

public interface UserService {

	UserResponse register(UserRegisterRequest request);

	UserResponse getUserById(Long id);

	UserLoginResponse login(UserLoginRequest request);
}
