package com.bezkoder.spring_boot_jpa_postgresql.service;

import java.util.List;
import java.util.Optional;

import com.bezkoder.spring_boot_jpa_postgresql.model.Tutorial;

public interface TutorialService {

    List<Tutorial> getAllTutorials(String title);

    Optional<Tutorial> getTutorialById(long id);

    Tutorial createTutorial(Tutorial tutorial);

    Optional<Tutorial> updateTutorial(long id, Tutorial tutorial);

    void deleteTutorial(long id);

    void deleteAllTutorials();

    List<Tutorial> findByPublished();
}
