package com.mickey.taskhub.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record CreateTaskRequest(
		@NotBlank @Size(max = 160) String title,
		@Size(max = 2000) String description,
		Set<Long> tagIds
) {
}
