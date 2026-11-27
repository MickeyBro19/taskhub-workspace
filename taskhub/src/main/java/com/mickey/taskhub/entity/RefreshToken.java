package com.mickey.taskhub.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(
		name = "refresh_tokens",
		indexes = {
				@Index(
						name = "idx_refresh_token_hash",
						columnList = "token_hash",
						unique = true
				)
		}
)
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private UserAccount user;
	
	@Column(name = "token_hash", nullable = false, unique = true)
	private String tokenHash;
	
	@Column(nullable = false)
	private Instant expiresAt;
	
	@Column
	private Instant revokedAt;
	
	@Column(nullable = false)
	private Instant createdAt = Instant.now();
	
}
