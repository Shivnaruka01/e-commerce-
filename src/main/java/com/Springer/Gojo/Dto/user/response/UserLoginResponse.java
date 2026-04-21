package com.Springer.Gojo.Dto.user.response;

import com.Springer.Gojo.entity.Role;

public record UserLoginResponse(

		Long userId, String email, Role role, String token) {

}
