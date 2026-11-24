package com.mickey.taskhub.dto.task;

import com.mickey.taskhub.entity.TaskStatus;

import java.time.Instant;
import java.util.List;

public record TaskResponse(
		Long id,
		String title,
		String description,
		TaskStatus status,
		Instant createdAt,
		List<String> tags
) {
}
