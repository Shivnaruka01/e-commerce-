package com.Springer.Gojo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	// Static factory method
	public static User createUser(String name, String email, String password) {
		return User.builder().name(name).email(email).password(password).role(Role.USER).build();
	}

	// Controlled updates
	public void updateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name cannot be empty");
		}
		this.name = name;
	}

	public void changePassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	public void promoteAdmin() {
		if (this.role != Role.ADMIN) {
			this.role = Role.ADMIN;
		}
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", role=" + role
				+ "]";
	}

}
