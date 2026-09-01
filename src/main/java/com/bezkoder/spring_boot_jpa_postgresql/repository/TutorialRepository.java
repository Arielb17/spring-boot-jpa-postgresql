package com.bezkoder.spring_boot_jpa_postgresql.repository;
import com.bezkoder.spring_boot_jpa_postgresql.model.Tutorial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByPublished(boolean published);

    List<Tutorial> findByTitleContaining(String title);
}