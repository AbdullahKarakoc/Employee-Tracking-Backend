package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.request.StatusRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.domain.response.StatusResponseDto;
import com.EmployeeTracking.enums.ProcessStatus;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
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
    private ProjectsResponseDto projectResponseDto;
    private StatusRequestDto statusRequestDto;
    private Status status;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // StatusRequestDto oluştur
        statusRequestDto = new StatusRequestDto();
        statusRequestDto.setStatus(ProcessStatus.IN_PROGRESS); // Örneğin bir enum kullanıyorsanız bu kısmı ayarlayın
        statusRequestDto.setDescription("In Progress");

        // ProjectsRequestDto oluştur
        projectRequestDto = new ProjectsRequestDto();
        projectRequestDto.setName("Test Project");
        projectRequestDto.setDescription("This is a test project");
        projectRequestDto.setStartDate(Instant.parse("2024-01-01T00:00:00Z"));
        projectRequestDto.setDeadline(Instant.parse("2024-06-01T00:00:00Z"));
        projectRequestDto.setFinishDate(Instant.parse("2024-05-01T00:00:00Z"));
        projectRequestDto.setStatus(statusRequestDto);

        // Status oluştur
        status = new Status();
        status.setStatus(ProcessStatus.IN_PROGRESS); // Enum kullanıyorsanız bunu ayarlayın
        status.setDescription("In Progress");

        // Projects oluştur
        project = new Projects();
        project.setProjectId(UUID.randomUUID());
        project.setName("Test Project");
        project.setDescription("This is a test project");
        project.setStartDate(Instant.parse("2024-01-01T00:00:00Z"));
        project.setDeadline(Instant.parse("2024-06-01T00:00:00Z"));
        project.setFinishDate(Instant.parse("2024-05-01T00:00:00Z"));
        project.setStatus(status);

        StatusResponseDto statusResponseDto = ObjectMapperUtils.map(status, StatusResponseDto.class);

        // ProjectsResponseDto oluştur
        projectResponseDto = new ProjectsResponseDto();
        projectResponseDto.setProjectId(project.getProjectId());
        projectResponseDto.setName(project.getName());
        projectResponseDto.setDescription(project.getDescription());
        projectResponseDto.setStartDate(project.getStartDate());
        projectResponseDto.setDeadline(project.getDeadline());
        projectResponseDto.setFinishDate(project.getFinishDate());
        projectResponseDto.setStatus(statusResponseDto); // StatusResponseDto nesnesini ayarlayın
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