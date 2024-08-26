package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.domain.model.*;
import com.EmployeeTracking.domain.request.ProjectRolesRequestDto;
import com.EmployeeTracking.domain.response.ProjectRolesResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectRolesRepository;
import com.EmployeeTracking.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

class ProjectRolesServiceTest {

    @Mock
    private ProjectRolesRepository projectRolesRepository;

    @Mock
    private ProjectsService projectsService;

    @InjectMocks
    private ProjectRolesService projectRolesService;

    private Projects project;
    private ProjectRoles projectRole;
    private ProjectRolesRequestDto projectRoleRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        Status status = TestDataFactory.createStatus();
        project = TestDataFactory.createProject(status);
        projectRole = TestDataFactory.createProjectRole();
        projectRole.setProject(project);

        projectRoleRequestDto = new ProjectRolesRequestDto();
        projectRoleRequestDto.setEmployeeRole("Developer"); // Example role, you can adjust accordingly
    }

    @Test
    void testGetAllProjectRoles() {
        // Arrange
        UUID projectId = project.getProjectId();
        when(projectRolesRepository.findByProject_ProjectId(projectId)).thenReturn(List.of(projectRole));

        // Act
        List<ProjectRolesResponseDto> response = projectRolesService.getAllProjectRoles(projectId);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
        assertEquals(1, response.size(), "Response size should be 1");
        assertEquals(projectRole.getEmployeeRole(), response.get(0).getEmployeeRole(), "Employee role does not match");

        verify(projectRolesRepository, times(1)).findByProject_ProjectId(projectId);
    }

    @Test
    void testGetProjectRoleById() {
        UUID projectId = project.getProjectId();
        UUID roleId = projectRole.getProjectRoleId();
        when(projectRolesRepository.findByProjectRoleIdAndProject_ProjectId(roleId, projectId))
                .thenReturn(Optional.of(projectRole));

        ProjectRolesResponseDto response = projectRolesService.getProjectRoleById(projectId, roleId);

        assertNotNull(response, "Response should not be null");
        assertEquals(projectRole.getProjectRoleId(), response.getProjectRoleId(), "Project role ID does not match");
        assertEquals(projectRole.getEmployeeRole(), response.getEmployeeRole(), "Employee role does not match");

        verify(projectRolesRepository, times(1)).findByProjectRoleIdAndProject_ProjectId(roleId, projectId);
    }

    @Test
    void testSaveProjectRole() {
        UUID projectId = project.getProjectId();
        when(projectsService.findById(projectId)).thenReturn(project);
        when(projectRolesRepository.saveAndFlush(any(ProjectRoles.class))).thenReturn(projectRole);

        ProjectRolesResponseDto response = projectRolesService.saveProjectRole(projectId, projectRoleRequestDto);

        assertNotNull(response, "Response should not be null");
        assertEquals(projectRole.getEmployeeRole(), response.getEmployeeRole(), "Employee role does not match");

        verify(projectsService, times(1)).findById(projectId);
        verify(projectRolesRepository, times(1)).saveAndFlush(any(ProjectRoles.class));
    }

    @Test
    void testUpdateProjectRole() {
        UUID projectId = project.getProjectId();
        UUID roleId = projectRole.getProjectRoleId();
        when(projectRolesRepository.findByProjectRoleIdAndProject_ProjectId(roleId, projectId))
                .thenReturn(Optional.of(projectRole));
        when(projectsService.findById(projectId)).thenReturn(project);
        when(projectRolesRepository.saveAndFlush(any(ProjectRoles.class))).thenReturn(projectRole);

        ProjectRolesResponseDto response = projectRolesService.updateProjectRole(projectId, roleId, projectRoleRequestDto);

        assertNotNull(response, "Response should not be null");
        assertEquals(projectRole.getEmployeeRole(), response.getEmployeeRole(), "Employee role does not match");

        verify(projectRolesRepository, times(1)).findByProjectRoleIdAndProject_ProjectId(roleId, projectId);
        verify(projectsService, times(1)).findById(projectId);
        verify(projectRolesRepository, times(1)).saveAndFlush(any(ProjectRoles.class));
    }

    @Test
    void testDeleteProjectRole() {
        UUID projectId = project.getProjectId();
        UUID roleId = projectRole.getProjectRoleId();
        when(projectRolesRepository.findByProjectRoleIdAndProject_ProjectId(roleId, projectId))
                .thenReturn(Optional.of(projectRole));

        projectRolesService.deleteProjectRole(projectId, roleId);

        verify(projectRolesRepository, times(1)).findByProjectRoleIdAndProject_ProjectId(roleId, projectId);
        verify(projectRolesRepository, times(1)).saveAndFlush(projectRole);
    }


    @Test
    void testFindByIdAndProjectId_roleNotFound() {
        UUID projectId = UUID.randomUUID();
        UUID roleId = UUID.randomUUID();
        when(projectRolesRepository.findByProjectRoleIdAndProject_ProjectId(roleId, projectId))
                .thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> projectRolesService.getProjectRoleById(projectId, roleId),
                "Expected DataNotFoundException to be thrown");

        verify(projectRolesRepository, times(1)).findByProjectRoleIdAndProject_ProjectId(roleId, projectId);
    }

    @Test
    void testGetAllProjectRolesWhenNoRolesExist() {
        UUID projectId = project.getProjectId();
        when(projectRolesRepository.findByProject_ProjectId(projectId)).thenReturn(List.of());

        List<ProjectRolesResponseDto> response = projectRolesService.getAllProjectRoles(projectId);

        assertNotNull(response, "Response should not be null");
        assertTrue(response.isEmpty(), "Response should be empty");
    }
}
