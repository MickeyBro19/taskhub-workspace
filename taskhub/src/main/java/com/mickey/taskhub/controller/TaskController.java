package com.mickey.taskhub.controller;

import com.mickey.taskhub.dto.task.CreateTaskRequest;
import com.mickey.taskhub.dto.task.TaskResponse;
import com.mickey.taskhub.dto.task.UpdateTaskRequest;
import com.mickey.taskhub.security.CustomUserPrincipal;
import com.mickey.taskhub.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
	
	private final TaskService taskService;
	
	@PostMapping
	public ResponseEntity<TaskResponse> createTask(
			@Valid @RequestBody CreateTaskRequest request,
			@AuthenticationPrincipal CustomUserPrincipal principal
	                                              ) {
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(principal.getId(), request));
	}
	
	@GetMapping
	public Page<TaskResponse> getMyTasks(
			@AuthenticationPrincipal CustomUserPrincipal principal,
			@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		return taskService.getMyTasks(principal.getId(), pageable);
	}
	
	@GetMapping("/{id}")
	public TaskResponse getMyTask(
			@PathVariable Long id,
			@AuthenticationPrincipal CustomUserPrincipal principal
	                             ) {
		return taskService.getMyTask(principal.getId(), id);
	}
	
	@PutMapping("/{id}")
	public TaskResponse updateTask(
			@PathVariable Long id,
			@Valid @RequestBody UpdateTaskRequest request,
			@AuthenticationPrincipal CustomUserPrincipal principal
	                              ) {
		
		return taskService.update(
				principal.getId(),
				id,
				request
		                         );
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTask(
			@PathVariable Long id,
			@AuthenticationPrincipal CustomUserPrincipal principal
	                      ) {
		
		taskService.delete(principal.getId(), id);
	}
}
