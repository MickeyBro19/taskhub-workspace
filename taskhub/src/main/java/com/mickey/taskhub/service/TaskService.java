package com.mickey.taskhub.service;

import com.mickey.taskhub.dto.task.CreateTaskRequest;
import com.mickey.taskhub.dto.task.TaskResponse;
import com.mickey.taskhub.dto.task.UpdateTaskRequest;
import com.mickey.taskhub.entity.Tag;
import com.mickey.taskhub.entity.TaskItem;
import com.mickey.taskhub.entity.TaskStatus;
import com.mickey.taskhub.entity.UserAccount;
import com.mickey.taskhub.exception.BadRequestException;
import com.mickey.taskhub.exception.NotFoundException;
import com.mickey.taskhub.repository.TagRepository;
import com.mickey.taskhub.repository.TaskItemRepository;
import com.mickey.taskhub.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskItemRepository taskItemRepository;
	private final TagRepository tagRepository;
	private final UserAccountRepository userAccountRepository;
	
	@Transactional
	public TaskResponse create(Long ownerId, CreateTaskRequest request) {
		UserAccount owner = userAccountRepository.findById(ownerId).orElseThrow(() -> new NotFoundException("User not Found"));
		TaskItem task = new TaskItem();
		task.setTitle(request.title().trim());
		task.setDescription(request.description());
		task.setStatus(TaskStatus.PENDING);
		task.setOwner(owner);
		task.setTags(resolveTags(request.tagIds()));
		
		return toResponse(taskItemRepository.save(task));
	}
	
	private Set<Tag> resolveTags(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return new HashSet<>();
		}
		List<Tag> found = tagRepository.findAllById(ids);
		if (found.size() != ids.size()) {
			throw new BadRequestException("One or more tag IDs are invalid");
		}
		return new HashSet<>(found);
		
	}
	
	private TaskResponse toResponse(TaskItem task) {
		return new TaskResponse(
				task.getId(),
				task.getTitle(),
				task.getDescription(),
				task.getStatus(),
				task.getCreatedAt(),
				task.getTags().stream().map(Tag :: getName).sorted().toList()
		);
	}
	
	@Transactional(readOnly = true)
	public Page<TaskResponse> getMyTasks(Long ownerId, Pageable pageable) {
		return taskItemRepository
				.findByOwnerId(ownerId, pageable)
				.map(this :: toResponse);
	}
	
	@Transactional(readOnly = true)
	public TaskResponse getMyTask(Long ownerId, Long taskId) {
		return taskItemRepository
				.findByIdAndOwnerId(taskId, ownerId)
				.map(this :: toResponse)
				.orElseThrow(() -> new BadRequestException("Task not found: " + taskId));
		
	}
	
	@Transactional
	public TaskResponse update(Long ownerId, Long taskId, UpdateTaskRequest request) {
		TaskItem task = taskItemRepository.findByIdAndOwnerId(taskId, ownerId).orElseThrow(() -> new BadRequestException("Task not found: " + taskId));
		task.setTitle(request.title());
		task.setDescription(request.description());
		task.setStatus(request.status());
		task.setTags(resolveTags(request.tagIds()));
		return toResponse(taskItemRepository.save(task));
	}
	
	@Transactional
	public void delete(Long taskId, Long ownerId) {
		TaskItem task = taskItemRepository.findByIdAndOwnerId(taskId, ownerId).orElseThrow(() -> new BadRequestException("Task not found: " + taskId));
		taskItemRepository.delete(task);
	}
}
