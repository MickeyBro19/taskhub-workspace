package com.mickey.taskhub.security;

import com.mickey.taskhub.entity.UserAccount;
import com.mickey.taskhub.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserAccountRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(@NonNull String userEmail) throws UsernameNotFoundException {
		String email = userEmail.trim().toLowerCase(Locale.ROOT);
		UserAccount user = repository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
		
		return new CustomUserPrincipal(
				user.getId(),
				user.getEmail(),
				user.getPasswordHash(),
				user.getRole(),
				Boolean.TRUE.equals(user.getEnabled())
		);
	}
}
