package com.mickey.taskhub.controller;

import com.mickey.taskhub.dto.auth.UserRegistrationRequest;
import com.mickey.taskhub.dto.user.UserResponse;
import com.mickey.taskhub.entity.UserAccount;
import com.mickey.taskhub.entity.UserProfile;
import com.mickey.taskhub.exception.BadRequestException;
import com.mickey.taskhub.repository.UserAccountRepository;
import com.mickey.taskhub.repository.UserProfileRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController("/api")
public class UserController {
	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;
	private final UserProfileRepository userProfileRepository;
	
	public UserController(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder, UserProfileRepository userProfileRepository) {
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = passwordEncoder;
		this.userProfileRepository = userProfileRepository;
	}
	
	@Transactional
	@PostMapping("/register")
	public UserResponse register(UserRegistrationRequest request){
		userAccountRepository.findByEmailIgnoreCase(request.email()).orElseThrow(()->new BadRequestException("Email already registered"));
		UserAccount account=new UserAccount();
		account.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
		account.setPasswordHash(passwordEncoder.encode(request.password()));
		
		UserProfile profile=new UserProfile();
		profile.setDisplayName(request.displayName());
		profile.setUser(account);
		account.setProfile(profile);
		userProfileRepository.save(profile);
		return toResponse(userAccountRepository.save(account));
		
	}
	
	private UserResponse toResponse(UserAccount account){
		return new UserResponse(
				account.getProfile().getDisplayName(),
				account.getEmail(),
				account.getProfile().getBio(),
				account.getRole(),
				account.getEnabled()
		);
	}
}

