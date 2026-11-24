package com.mickey.taskhub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationRequest(
		@NotBlank(message = "Email is required")
		@Email(message = "Invalid email format")
		@Size(max = 120)
		String email,
		
		@NotBlank(message = "Password is required")
		@Size(min = 8, max = 72)
		String password,
		
		@NotBlank(message = "Display name is required")
		@Size(max = 80)
		String displayName
) {
}
