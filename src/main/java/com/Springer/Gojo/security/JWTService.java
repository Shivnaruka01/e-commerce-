package com.Springer.Gojo.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.Springer.Gojo.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expirationTime;

	public String generateToken(User user) {
		return Jwts.builder()
				.setSubject(user.getEmail())
				.claim("role", "ROLE_" + user.getRole().name())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expirationTime))
				.signWith(getSignKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	private Key getSignKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String extractRole(String token) {
		return extractClaim(token, claim -> claim.get("role", String.class));
	}
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		final Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean isTokenValid(String token, User user) {
		String username = extractUsername(token);
		return username.equals(user.getEmail()) && !isTokenExpired(token);
	}

	public boolean isTokenExpired(String token) {
		return extractClaim(token, Claims::getExpiration).before(new Date());
	}
}
/*
 * 1. .setSubject(user.getEmail()): This defines who the token belongs to. We
 * use the email because it is unique.
 * 
 * 2. .claim("role", ...): This adds custom data. By putting the role in the
 * token, the server doesn't have to check the database on every single request
 * to know what the user is allowed to do.
 * 
 * 3. .setIssuedAt(...): The exact time the token was created.
 * 
 * 4. .setExpiration(...): When the "passport" expires. In your code, it's
 * 3,600,000 milliseconds (1 hour). After this, the user must log in again. This
 * limits the damage if a token is stolen.
 * 
 * 5. getSignKey(): Converts your String key into a format the library can use
 * for math (a Key object).
 * 
 * 6. SignatureAlgorithm.HS256: This is the mathematical formula used to combine
 * the data and your secret key into a unique signature.
 * 
 * 7. .compact(): This is the final step that squashes all the data, headers,
 * and signature into the three-part string (e.g., xxxxx.yyyyy.zzzzz) that the
 * client receives.
 * 
 */
