package com.Springer.Gojo.Dto.user.response;

import com.Springer.Gojo.entity.Role;

public record UserResponse(

		Long id, String name, String email, Role role) {

}
