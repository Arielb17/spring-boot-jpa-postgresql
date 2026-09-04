package com.bezkoder.spring_boot_jpa_postgresql.repository;
import com.bezkoder.spring_boot_jpa_postgresql.model.Tutorial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {
    List<Tutorial> findByPublished(boolean published);

    @Query("SELECT t FROM Tutorial t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Tutorial> findByTitle(@Param("title") String title);

    List<Tutorial> findByTitleContaining(String title);
}
