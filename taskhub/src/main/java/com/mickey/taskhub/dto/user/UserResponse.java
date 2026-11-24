package com.mickey.taskhub.dto.user;

import com.mickey.taskhub.entity.Role;

public record UserResponse(
		String displayName,
		String email,
		String bio,
		Role role,
		Boolean enabled
		
) {
}
