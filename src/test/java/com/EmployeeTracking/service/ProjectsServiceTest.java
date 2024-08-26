package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectsRepository;
import com.EmployeeTracking.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ProjectsServiceTest {

    @Mock
    private ProjectsRepository projectsRepository;

    @InjectMocks
    private ProjectsService projectsService;

    private Projects project;
    private ProjectsRequestDto projectRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Status status = TestDataFactory.createStatus();
        projectRequestDto = TestDataFactory.createProjectRequestDto();
        project = TestDataFactory.createProject(status);
    }

    @Test
    void testSaveProject() {
        when(projectsRepository.saveAndFlush(any(Projects.class))).thenReturn(project);

        ProjectsResponseDto response = projectsService.saveProject(projectRequestDto);

        assertNotNull(response);
        assertEquals(project.getName(), response.getName());
        assertEquals(project.getDescription(), response.getDescription());

        verify(projectsRepository, times(1)).saveAndFlush(any(Projects.class));
    }

    @Test
    void testUpdateProject() {
        UUID id = project.getProjectId();
        when(projectsRepository.findById(id)).thenReturn(Optional.of(project));
        when(projectsRepository.saveAndFlush(any(Projects.class))).thenReturn(project);

        ProjectsResponseDto response = projectsService.updateProject(id, projectRequestDto);

        assertNotNull(response);
        assertEquals(project.getProjectId(), response.getProjectId());
        assertEquals(project.getName(), response.getName());

        verify(projectsRepository, times(1)).findById(id);
        verify(projectsRepository, times(1)).saveAndFlush(any(Projects.class));
    }

    @Test
    void testDeleteProject() {
        UUID id = project.getProjectId();
        when(projectsRepository.findById(id)).thenReturn(Optional.of(project));

        projectsService.deleteProject(id);

        assertTrue(project.isDeleted());
        verify(projectsRepository, times(1)).saveAndFlush(any(Projects.class));
    }

    @Test
    void testGetAllProjects() {
        when(projectsRepository.findAll()).thenReturn(List.of(project));

        List<ProjectsResponseDto> response = projectsService.getAllProjects();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(project.getName(), response.get(0).getName());

        verify(projectsRepository, times(1)).findAll();
    }

    @Test
    void testGetProjectById() {
        UUID id = project.getProjectId();
        when(projectsRepository.findById(id)).thenReturn(Optional.of(project));

        ProjectsResponseDto response = projectsService.getProjectById(id);

        assertNotNull(response);
        assertEquals(project.getProjectId(), response.getProjectId());
        assertEquals(project.getName(), response.getName());

        verify(projectsRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_projectNotFound() {
        UUID id = UUID.randomUUID();
        when(projectsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> projectsService.findById(id));

        verify(projectsRepository, times(1)).findById(id);
    }
}
