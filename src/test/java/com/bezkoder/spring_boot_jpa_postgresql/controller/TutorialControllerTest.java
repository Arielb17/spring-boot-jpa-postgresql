package com.bezkoder.spring_boot_jpa_postgresql.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bezkoder.spring_boot_jpa_postgresql.model.Tutorial;
import com.bezkoder.spring_boot_jpa_postgresql.service.TutorialService;

class TutorialControllerTest {

    private TutorialService tutorialService;
    private TutorialController tutorialController;

    @BeforeEach
    void setUp() {
        tutorialService = mock(TutorialService.class);
        tutorialController = new TutorialController(tutorialService);
    }

    @Test
    void getAllTutorialsReturnsOkWithTutorials() {
        List<Tutorial> tutorials = List.of(new Tutorial("Spring", "REST API", true));
        when(tutorialService.getAllTutorials(null)).thenReturn(tutorials);

        ResponseEntity<List<Tutorial>> response = tutorialController.getAllTutorials(null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(tutorials);
    }

    @Test
    void getAllTutorialsReturnsOkWithEmptyListAndPreservesTitleFilter() {
        List<Tutorial> tutorials = Collections.emptyList();
        when(tutorialService.getAllTutorials("Spring")).thenReturn(tutorials);

        ResponseEntity<List<Tutorial>> response = tutorialController.getAllTutorials("Spring");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
        verify(tutorialService).getAllTutorials("Spring");
    }

    @Test
    void getAllTutorialsReturnsInternalServerErrorOnUnexpectedError() {
        when(tutorialService.getAllTutorials(null)).thenThrow(new RuntimeException());

        ResponseEntity<List<Tutorial>> response = tutorialController.getAllTutorials(null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void findByExactTitleReturnsOkWithTutorialsAndForwardsTitle() {
        List<Tutorial> tutorials = List.of(new Tutorial("Spring", "REST API", true));
        when(tutorialService.findByExactTitle("Spring")).thenReturn(tutorials);

        ResponseEntity<List<Tutorial>> response = tutorialController.findByExactTitle("Spring");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(tutorials);
        verify(tutorialService).findByExactTitle("Spring");
    }

    @Test
    void findByExactTitleReturnsOkWithEmptyList() {
        when(tutorialService.findByExactTitle("Missing")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Tutorial>> response = tutorialController.findByExactTitle("Missing");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void findByExactTitleReturnsInternalServerErrorOnUnexpectedError() {
        when(tutorialService.findByExactTitle("Spring")).thenThrow(new RuntimeException());

        ResponseEntity<List<Tutorial>> response = tutorialController.findByExactTitle("Spring");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getTutorialByIdReturnsOkWhenFound() {
        Tutorial tutorial = new Tutorial("Spring", "REST API", true);
        when(tutorialService.getTutorialById(1L)).thenReturn(Optional.of(tutorial));

        ResponseEntity<Tutorial> response = tutorialController.getTutorialById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(tutorial);
    }

    @Test
    void getTutorialByIdReturnsNotFoundWhenMissing() {
        when(tutorialService.getTutorialById(99L)).thenReturn(Optional.empty());

        ResponseEntity<Tutorial> response = tutorialController.getTutorialById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void createTutorialReturnsCreated() {
        Tutorial request = new Tutorial("Spring", "REST API", false);
        Tutorial created = new Tutorial("Spring", "REST API", false);
        when(tutorialService.createTutorial(request)).thenReturn(created);

        ResponseEntity<Tutorial> response = tutorialController.createTutorial(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(created);
    }

    @Test
    void createTutorialReturnsInternalServerErrorOnUnexpectedError() {
        Tutorial tutorial = new Tutorial("Spring", "REST API", false);
        when(tutorialService.createTutorial(tutorial)).thenThrow(new RuntimeException());

        ResponseEntity<Tutorial> response = tutorialController.createTutorial(tutorial);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void updateTutorialReturnsOkWhenFound() {
        Tutorial request = new Tutorial("Updated", "Updated description", true);
        Tutorial updated = new Tutorial("Updated", "Updated description", true);
        when(tutorialService.updateTutorial(1L, request)).thenReturn(Optional.of(updated));

        ResponseEntity<Tutorial> response = tutorialController.updateTutorial(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(updated);
    }

    @Test
    void updateTutorialReturnsNotFoundWhenMissing() {
        Tutorial request = new Tutorial("Updated", "Updated description", true);
        when(tutorialService.updateTutorial(99L, request)).thenReturn(Optional.empty());

        ResponseEntity<Tutorial> response = tutorialController.updateTutorial(99L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteTutorialReturnsNoContent() {
        ResponseEntity<Void> response = tutorialController.deleteTutorial(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(tutorialService).deleteTutorial(1L);
    }

    @Test
    void deleteTutorialReturnsInternalServerErrorOnUnexpectedError() {
        doThrow(new RuntimeException()).when(tutorialService).deleteTutorial(1L);

        ResponseEntity<Void> response = tutorialController.deleteTutorial(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void deleteAllTutorialsReturnsNoContent() {
        ResponseEntity<Void> response = tutorialController.deleteAllTutorials();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        verify(tutorialService).deleteAllTutorials();
    }

    @Test
    void deleteAllTutorialsReturnsInternalServerErrorOnUnexpectedError() {
        doThrow(new RuntimeException()).when(tutorialService).deleteAllTutorials();

        ResponseEntity<Void> response = tutorialController.deleteAllTutorials();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void findByPublishedReturnsOkWithTutorials() {
        List<Tutorial> tutorials = List.of(new Tutorial("Spring", "REST API", true));
        when(tutorialService.findByPublished()).thenReturn(tutorials);

        ResponseEntity<List<Tutorial>> response = tutorialController.findByPublished();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(tutorials);
    }

    @Test
    void findByPublishedReturnsOkWithEmptyList() {
        when(tutorialService.findByPublished()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Tutorial>> response = tutorialController.findByPublished();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void findByPublishedReturnsInternalServerErrorOnUnexpectedError() {
        when(tutorialService.findByPublished()).thenThrow(new RuntimeException());

        ResponseEntity<List<Tutorial>> response = tutorialController.findByPublished();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNull();
    }
}
