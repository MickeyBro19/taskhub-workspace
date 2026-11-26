package com.mickey.taskhub.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "task_items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, length = 160)
	private String title;
	
	@Column(length = 2000)
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private TaskStatus status;
	
	@Column(nullable = false)
	private Instant createdAt = Instant.now();
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "owner_id")
	private UserAccount owner;
	
	@ManyToMany
	@JoinTable(
			name = "task_tags",
			joinColumns = @JoinColumn(name = "task_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_id")
	)
	private Set<Tag> tags = new HashSet<>();
}
