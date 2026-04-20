package com.Springer.Gojo.mapper;

import org.mapstruct.Mapper;

import com.Springer.Gojo.Dto.user.request.UserRegisterRequest;
import com.Springer.Gojo.Dto.user.response.UserResponse;
import com.Springer.Gojo.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	// Convert Request DTO → Entity
	// (This is hard coded role in mapper, this will get replace by creatUser method
	// in user )

//	@Mapping(target = "password", source = "encodedPassword")
//	@Mapping(target = "role", constant = "USER")
	// User toEntity(UserRegisterRequest request, String encodedPassword);

	// This approach is not good because internally MapStruct use new User() +
	// setters. To stop using setters so that not anyone can change data inside we
	// avoid setters which MapStruct fail to do.

	default User toEntity(UserRegisterRequest request, String encodedPassword) {
		return User.createUser(request.name(), request.email(), encodedPassword);

	}

	// Convert Entity to Response DTO
	UserResponse toResponse(User user);

}
/*
 * 
 * METHOD - 1
 * 
 * WHY static methods?
 * 
 * No need to create object every time Stateless → clean and fast Common
 * industry practice for simple mappers
 * 
 * // Convert Request DTO → Entity public static User
 * toEntity(UserRegisterRequest request, String encodedPassword) { return
 * User.builder() .name(request.name()) .email(request.email())
 * .password(encodedPassword) .role(Role.USER) .build(); }
 * 
 * // Convert Entity to Response DTO public static UserResponse toResponse(User
 * user) { return new UserResponse( user.getId(), user.getName(),
 * user.getEmail(), user.getRole().name() ); }
 * 
 * 
 * METHOD - 2
 * 
 * BUT :
 * 
 * If your mapping becomes complex later (e.g., needs services), convert it into
 * a Spring bean. Remove static from class then.
 * 
 * // Convert Request DTO → Entity public User toEntity(UserRegisterRequest
 * request, String encodedPassword) { return User.builder()
 * .name(request.name()) .email(request.email()) .password(encodedPassword)
 * .role(Role.USER) .build(); }
 * 
 * // Convert Entity to Response DTO public UserResponse toResponse(User user) {
 * return new UserResponse( user.getId(), user.getName(), user.getEmail(),
 * user.getRole().name() ); }
 * 
 * 
 * METHOD - 3
 * 
 * Import dependency MapStruct and use annotation
 */