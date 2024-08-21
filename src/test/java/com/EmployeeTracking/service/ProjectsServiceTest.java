package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.request.StatusRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.repository.ProjectsRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectsServiceTest {

    @Mock
    private ProjectsRepository projectsRepository;

    @Mock
    private ModelMapper modelMapper;

    private ProjectsService projectsService;

    @BeforeEach
    public void setupTest() {
        projectsRepository = mock(ProjectsRepository.class);
        modelMapper = mock(ModelMapper.class);
        projectsService = new ProjectsService(projectsRepository, modelMapper);
    }

    @AfterEach
    public void tearDown() {
        projectsService = null;
        projectsRepository = null;
        modelMapper = null;
    }

    @Test
    public void Should_Success_When_SaveProject() {
        // arrange
        UUID projectId = UUID.randomUUID();
        ProjectsRequestDto requestDto = new ProjectsRequestDto();
        requestDto.setName("Project Name");
        requestDto.setDescription("Project Description");
        requestDto.setStartDate(Instant.now());
        requestDto.setDeadline(Instant.now().plusSeconds(3600));
        requestDto.setFinishDate(Instant.now().plusSeconds(7200));
        requestDto.setStatus(new StatusRequestDto());

        Projects projectEntity = new Projects();
        projectEntity.setProjectId(projectId);
        projectEntity.setName("Project Name");
        projectEntity.setDescription("Project Description");
        projectEntity.setStartDate(Instant.now());
        projectEntity.setDeadline(Instant.now().plusSeconds(3600));
        projectEntity.setFinishDate(Instant.now().plusSeconds(7200));
        projectEntity.setStatus(new Status());

        Projects savedProject = new Projects();
        savedProject.setProjectId(projectId);
        savedProject.setName("Project Name");
        savedProject.setDescription("Project Description");
        savedProject.setStartDate(Instant.now());
        savedProject.setDeadline(Instant.now().plusSeconds(3600));
        savedProject.setFinishDate(Instant.now().plusSeconds(7200));
        savedProject.setStatus(new Status());

        ProjectsResponseDto responseDto = new ProjectsResponseDto();
        responseDto.setProjectId(projectId);
        responseDto.setName("Project Name");
        responseDto.setDescription("Project Description");

        when(modelMapper.map(requestDto, Projects.class)).thenReturn(projectEntity);
        when(projectsRepository.saveAndFlush(any(Projects.class))).thenReturn(savedProject);
        when(modelMapper.map(savedProject, ProjectsResponseDto.class)).thenReturn(responseDto);

        // act
        ProjectsResponseDto result = projectsService.saveProject(requestDto);

        // assert
        verify(projectsRepository, times(1)).saveAndFlush(projectEntity);
        assertEquals(responseDto, result);
    }

    @Test
    public void Should_ThrowException_When_SaveProjectWithNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            projectsService.saveProject(null);
        });

        verify(projectsRepository, times(0)).save(any());
    }
}
