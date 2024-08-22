package com.EmployeeTracking.service;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectsRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectsServiceTest {

    @Mock
    private ProjectsRepository projectsRepository;

    @InjectMocks
    private ProjectsService projectsService;

    private AutoCloseable closeable;

    @BeforeEach
    public void setupTest() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void Should_Success_When_SaveProject() {
        try (var mock = mockStatic(ObjectMapperUtils.class)) {
            // Arrange
            UUID projectId = UUID.randomUUID();
            ProjectsRequestDto requestDto = new ProjectsRequestDto();
            requestDto.setName("Project Name");
            requestDto.setDescription("Project Description");
            requestDto.setStartDate(Instant.now());
            requestDto.setDeadline(Instant.now().plusSeconds(3600));
            requestDto.setFinishDate(Instant.now().plusSeconds(7200));

            Projects projectEntity = new Projects();
            projectEntity.setProjectId(projectId);
            projectEntity.setName("Project Name");
            projectEntity.setDescription("Project Description");
            projectEntity.setStartDate(Instant.now());
            projectEntity.setDeadline(Instant.now().plusSeconds(3600));
            projectEntity.setFinishDate(Instant.now().plusSeconds(7200));

            Projects savedProject = new Projects();
            savedProject.setProjectId(projectId);
            savedProject.setName("Project Name");
            savedProject.setDescription("Project Description");
            savedProject.setStartDate(Instant.now());
            savedProject.setDeadline(Instant.now().plusSeconds(3600));
            savedProject.setFinishDate(Instant.now().plusSeconds(7200));

            ProjectsResponseDto responseDto = new ProjectsResponseDto();
            responseDto.setProjectId(projectId);
            responseDto.setName("Project Name");
            responseDto.setDescription("Project Description");

            // Stubbing ObjectMapperUtils.map
            when(ObjectMapperUtils.map(requestDto, Projects.class)).thenReturn(projectEntity);
            when(ObjectMapperUtils.map(savedProject, ProjectsResponseDto.class)).thenReturn(responseDto);

            // Stubbing ProjectsRepository.saveAndFlush
            when(projectsRepository.saveAndFlush(any(Projects.class))).thenReturn(savedProject);

            // Act
            ProjectsResponseDto result = projectsService.saveProject(requestDto);

            // Assert
            verify(projectsRepository, times(1)).saveAndFlush(argThat(project -> project.getProjectId() != null
                    && "Project Name".equals(project.getName())
                    && "Project Description".equals(project.getDescription())
                    && project.getStartDate() != null
                    && project.getDeadline() != null
                    && project.getFinishDate() != null));
            assertEquals(responseDto, result);
        }
    }

    @Test
    public void Should_ThrowException_When_SaveProjectWithNullParameter() {
        assertThrows(IllegalArgumentException.class, () -> {
            projectsService.saveProject(null);
        });

        verify(projectsRepository, times(0)).saveAndFlush(any());
    }

    @Test
    public void Should_ReturnEmptyList_When_NoProjectsFound() {
        // Arrange
        when(projectsRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ProjectsResponseDto> result = projectsService.getAllProjects();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    public void Should_ReturnProjectsList_When_ProjectsFound() {
        try (var mock = mockStatic(ObjectMapperUtils.class)) {
            // Arrange
            UUID projectId = UUID.randomUUID();
            Projects project = new Projects();
            project.setProjectId(projectId);
            project.setName("Project Name");

            ProjectsResponseDto responseDto = new ProjectsResponseDto();
            responseDto.setProjectId(projectId);
            responseDto.setName("Project Name");

            // Stubbing ProjectsRepository.findAll
            when(projectsRepository.findAll()).thenReturn(List.of(project));

            // Stubbing ObjectMapperUtils.mapAll
            when(ObjectMapperUtils.mapAll(any(), eq(ProjectsResponseDto.class)))
                    .thenAnswer(new Answer<List<ProjectsResponseDto>>() {
                        @Override
                        public List<ProjectsResponseDto> answer(InvocationOnMock invocation) throws Throwable {
                            @SuppressWarnings("unchecked")
                            List<Projects> projects = (List<Projects>) invocation.getArguments()[0];
                            return projects.stream()
                                    .map(p -> ObjectMapperUtils.map(p, ProjectsResponseDto.class))
                                    .collect(Collectors.toList());
                        }
                    });

            // Act
            List<ProjectsResponseDto> result = projectsService.getAllProjects();

            // Assert
            assertEquals(1, result.size());
            assertEquals(responseDto, result.get(0));
        }
    }

    @Test
    public void Should_ThrowException_When_ProjectNotFound() {
        // Arrange
        UUID projectId = UUID.randomUUID();
        when(projectsRepository.findById(projectId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(DataNotFoundException.class, () -> projectsService.findById(projectId));
    }
}
