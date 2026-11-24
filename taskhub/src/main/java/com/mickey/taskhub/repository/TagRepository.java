package com.mickey.taskhub.repository;

import com.mickey.taskhub.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
	Optional<Tag> findByNameIgnoreCase(String name);
}
