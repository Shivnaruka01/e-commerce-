package com.Springer.Gojo.security;

import java.io.IOException;

import java.util.List;

import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j // Use Lombok for for the logger
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JWTService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");

		try {
			// 1. Check header
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}

			// 2. Extract token
			String token = authHeader.substring(7);

			// 3. Extract email
			String email = jwtService.extractUsername(token);

			// Extract Role
			String role = jwtService.extractRole(token);

			// 4. If not already authenticated
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				// User user = userRepository.findByEmail(email).orElseThrow();

				// validate token
				if (!jwtService.isTokenExpired(token)) {

					// Inject context.logs
					MDC.put("userEmail", email);

					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null,
							List.of(new SimpleGrantedAuthority(role)));

					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authToken);

					log.atDebug().setMessage("User authenticated successfully").addKeyValue("email", email)
					.addKeyValue("role", role).log();
				}

			}
			filterChain.doFilter(request, response);
		} finally {
			MDC.clear(); // CRITICAL: Always clear MDC in 'finally' so the thread is clean for the next
			// user

		}

	}

}

//  use @Slf4j to avoid the old private static final Logger... boilerplate.