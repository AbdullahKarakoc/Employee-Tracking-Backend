package com.EmployeeTracking.service;


import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.ProjectRoles;
import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.request.ProjectRolesRequestDto;
import com.EmployeeTracking.domain.response.ProjectRolesResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectRolesRepository;
import com.EmployeeTracking.util.AppUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProjectRolesService {

    private final ProjectsService projectsService;
    private final ProjectRolesRepository projectRolesRepository;

    public List<ProjectRolesResponseDto> getAllProjectRoles(UUID projectId) {
        List<ProjectRoles> projectRoles = projectRolesRepository.findByProject_ProjectId(projectId);

        if (projectRoles.isEmpty()) {
            return AppUtils.emptyList();
        }

        return ObjectMapperUtils.mapAll(projectRoles, ProjectRolesResponseDto.class);
    }

    public ProjectRolesResponseDto getProjectRoleById(UUID projectId, UUID roleId) {
        ProjectRoles projectRole = findByIdAndProjectId(roleId, projectId);
        return ObjectMapperUtils.map(projectRole, ProjectRolesResponseDto.class);
    }

    public ProjectRolesResponseDto saveProjectRole(UUID projectId, ProjectRolesRequestDto requestDto) {
        Projects project = projectsService.findById(projectId);

        ProjectRoles projectRole = ObjectMapperUtils.map(requestDto, ProjectRoles.class);
        projectRole.setProject(project);

        ProjectRoles savedProjectRole = save(projectRole);
        return ObjectMapperUtils.map(savedProjectRole, ProjectRolesResponseDto.class);
    }

    @Transactional
    public ProjectRolesResponseDto updateProjectRole(UUID projectId, UUID roleId, ProjectRolesRequestDto requestDto) {
        ProjectRoles existingProjectRole = findByIdAndProjectId(roleId, projectId);

        existingProjectRole.setEmployeeRole(requestDto.getEmployeeRole());
        existingProjectRole.setDescription(requestDto.getDescription());
        existingProjectRole.setProject(projectsService.findById(projectId));

        ProjectRoles updatedProjectRole = save(existingProjectRole);
        return ObjectMapperUtils.map(updatedProjectRole, ProjectRolesResponseDto.class);
    }

    public void deleteProjectRole(UUID projectId, UUID roleId) {
        ProjectRoles projectRole = findByIdAndProjectId(roleId, projectId);
        projectRole.setDeleted(true);
        save(projectRole);
    }

    public ProjectRoles save(ProjectRoles projectRole) {
        return projectRolesRepository.saveAndFlush(projectRole);
    }

    public ProjectRoles findByIdAndProjectId(UUID roleId, UUID projectId) {
        return projectRolesRepository.findByProjectRoleIdAndProject_ProjectId(roleId, projectId)
                .orElseThrow(() -> new DataNotFoundException("Project role not found for the given project"));
    }
}
