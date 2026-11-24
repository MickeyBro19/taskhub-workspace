package com.mickey.taskhub.dto.auth;

public record AuthResponse(
		String accessToken,
		String tokenType,
		Long expiresInSeconds
) {
}
