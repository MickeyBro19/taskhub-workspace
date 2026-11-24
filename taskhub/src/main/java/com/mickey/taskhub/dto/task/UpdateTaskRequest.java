package com.mickey.taskhub.dto.task;

import com.mickey.taskhub.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UpdateTaskRequest(
		@NotBlank @Size(max = 160) String title,
		@Size(max = 2000) String description,
		@NotNull TaskStatus status,
		Set<Long> tagIds
) {
}
