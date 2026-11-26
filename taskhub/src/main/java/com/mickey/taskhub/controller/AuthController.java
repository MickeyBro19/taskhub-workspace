package com.mickey.taskhub.controller;

import com.mickey.taskhub.dto.auth.AuthResponse;
import com.mickey.taskhub.dto.auth.LoginRequest;
import com.mickey.taskhub.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final long accessTokenExpirationMs;
	
	public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, @Value("${app.jwt.access-token-expiration-ms}") long accessTokenExpirationMs) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.accessTokenExpirationMs = accessTokenExpirationMs;
	}
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		String email = loginRequest.email().trim().toLowerCase(Locale.ROOT);
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							email,
							loginRequest.password()
					));
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String accessToken = jwtService.generateAccessToken(userDetails);
			AuthResponse response = new AuthResponse(
					accessToken,
					"Bearer",
					accessTokenExpirationMs/1000
					);
			return ResponseEntity.ok(response);
		}catch (AuthenticationException e){
			throw new ResponseStatusException(
					HttpStatus.UNAUTHORIZED,
					"Invalid email or password"
			);
		}
	}
}
