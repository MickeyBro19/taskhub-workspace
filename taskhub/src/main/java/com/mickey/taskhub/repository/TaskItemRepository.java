package com.mickey.taskhub.repository;

import com.mickey.taskhub.entity.TaskItem;
import com.mickey.taskhub.entity.TaskStatus;
import com.mickey.taskhub.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskItemRepository extends JpaRepository<TaskItem,Long> {
	Page<TaskItem> findByOwnerId(Long ownerId, Pageable pageable);
	Optional<TaskItem> findByIdAndOwnerId(Long taskId, Long ownerId);
	List<TaskItem> findByOwnerIdAndStatus(Long ownerId, TaskStatus status);
	
	Long owner(UserAccount owner);
}
