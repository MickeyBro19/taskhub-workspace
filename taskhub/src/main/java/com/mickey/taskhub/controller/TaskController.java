package com.mickey.taskhub.controller;

import com.mickey.taskhub.dto.task.CreateTaskRequest;
import com.mickey.taskhub.dto.task.TaskResponse;
import com.mickey.taskhub.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
	private final TaskService taskService;
	
	@GetMapping
	public ResponseEntity<TaskResponse> createTask(@Valid CreateTaskRequest request){
		return ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(request));
	}
}
