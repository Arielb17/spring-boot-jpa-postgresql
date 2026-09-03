package com.bezkoder.spring_boot_jpa_postgresql.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bezkoder.spring_boot_jpa_postgresql.model.Tutorial;
import com.bezkoder.spring_boot_jpa_postgresql.repository.TutorialRepository;

class TutorialServiceImplTest {

    private TutorialRepository tutorialRepository;
    private TutorialServiceImpl tutorialService;

    @BeforeEach
    void setUp() {
        tutorialRepository = mock(TutorialRepository.class);
        tutorialService = new TutorialServiceImpl(tutorialRepository);
    }

    @Test
    void findByExactTitleUsesExactRepositorySearch() {
        List<Tutorial> tutorials = List.of(new Tutorial("Spring", "REST API", true));
        when(tutorialRepository.findByTitle("Spring")).thenReturn(tutorials);

        List<Tutorial> result = tutorialService.findByExactTitle("Spring");

        assertThat(result).isEqualTo(tutorials);
        verify(tutorialRepository).findByTitle("Spring");
        verify(tutorialRepository, never()).findByTitleContaining("Spring");
    }
}
