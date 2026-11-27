package com.mickey.taskhub.security;

import com.mickey.taskhub.entity.Role;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserPrincipal implements UserDetails {
	@Getter
	private final Long id;
	private final String email;
	private final String passwordHash;
	private final Role role;
	private final boolean enabled;
	
	public CustomUserPrincipal(Long id, String email, String passwordHash, Role role, boolean enabled) {
		this.id = id;
		this.email = email;
		this.passwordHash = passwordHash;
		this.role = role;
		this.enabled = enabled;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("ROLE" + role.name()));
	}
	
	@Override
	public @Nullable String getPassword() {
		return passwordHash;
	}
	
	@Override
	public String getUsername() {
		return email;
	}
	
	
}
