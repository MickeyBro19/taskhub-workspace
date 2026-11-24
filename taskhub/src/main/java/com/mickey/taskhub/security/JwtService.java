package com.mickey.taskhub.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
	String generateAccessToken(UserDetails user);
	String extractSubject(String token);
	boolean isTokenValid(String token, UserDetails user);
}
