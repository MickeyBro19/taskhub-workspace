package com.mickey.taskhub.security;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

public class JwtServiceImpl implements  JwtService {
	private final SecretKey signingKey;
	private final Long expirationIn;
	
	public JwtServiceImpl(
			@Value("${a}") SecretKey signingKey, Long expirationIn) {}
	
	@Override
	public String generateAccessToken(UserDetails user) {
		return "";
	}
	
	@Override
	public String extractSubject(String token) {
		return "";
	}
	
	@Override
	public boolean isTokenValid(String token, UserDetails user) {
		return false;
	}
}
