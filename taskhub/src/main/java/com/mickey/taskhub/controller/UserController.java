package com.mickey.taskhub.controller;

import com.mickey.taskhub.dto.user.UserRegistrationRequest;
import com.mickey.taskhub.dto.user.UserResponse;
import com.mickey.taskhub.entity.UserAccount;
import com.mickey.taskhub.entity.UserProfile;
import com.mickey.taskhub.exception.BadRequestException;
import com.mickey.taskhub.repository.UserAccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserController(
			UserAccountRepository userAccountRepository,
			PasswordEncoder passwordEncoder
	                     ) {
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@Transactional
	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(
			@Valid @RequestBody UserRegistrationRequest request
	                                            ) {
		String email = request.email().trim().toLowerCase(Locale.ROOT);
		if (userAccountRepository.existsByEmailIgnoreCase(email)) {
			throw new BadRequestException("User already exits");
		}
		
		UserAccount account = new UserAccount();
		account.setEmail(email);
		account.setPasswordHash(passwordEncoder.encode(request.password()));
		
		UserProfile profile = new UserProfile();
		profile.setDisplayName(request.displayName().trim());
		
		// Maintain both sides of the relationship.
		account.setProfile(profile);
		profile.setUser(account);
		
		// Cascade persists the associated profile
		UserAccount savedAccount = userAccountRepository.save(account);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(toResponse(savedAccount));
	}
	
	
	private UserResponse toResponse(UserAccount account) {
		return new UserResponse(
				account.getProfile().getDisplayName(),
				account.getEmail(),
				account.getProfile().getBio(),
				account.getRole(),
				account.getEnabled()
		);
	}
}

