package com.EmployeeTracking.service;


import com.EmployeeTracking.domain.model.ProjectRoles;
import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.request.ProjectRolesRequestDto;
import com.EmployeeTracking.domain.response.ProjectRolesResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectRolesRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectRolesService {

    private final ProjectRolesRepository projectRolesRepository;
    private final ProjectsService projectsService;
    private final ModelMapper modelMapper;

    public List<ProjectRolesResponseDto> getAllProjectRoles() {
        List<ProjectRoles> projectRoles = projectRolesRepository.findAll();
        return projectRoles.stream()
                .map(projectRole -> modelMapper.map(projectRole, ProjectRolesResponseDto.class))
                .collect(Collectors.toList());
    }

    public ProjectRolesResponseDto getProjectRoleById(UUID id) {
        ProjectRoles projectRole = findById(id);
        return modelMapper.map(projectRole, ProjectRolesResponseDto.class);
    }

    public ProjectRolesResponseDto saveProjectRole(ProjectRolesRequestDto projectRolesRequestDto) {
        Projects project = projectsService.findById(projectRolesRequestDto.getProjectId());

        ProjectRoles projectRole = modelMapper.map(projectRolesRequestDto, ProjectRoles.class);
        projectRole.setProject(project);

        ProjectRoles savedProjectRole = save(projectRole);
        return modelMapper.map(savedProjectRole, ProjectRolesResponseDto.class);
    }

    public ProjectRolesResponseDto updateProjectRole(UUID id, ProjectRolesRequestDto projectRolesRequestDto) {
        ProjectRoles existingProjectRole = findById(id);
        Projects project = projectsService.findById(projectRolesRequestDto.getProjectId());

        modelMapper.map(projectRolesRequestDto, existingProjectRole);
        existingProjectRole.setProject(project);

        ProjectRoles updatedProjectRole = save(existingProjectRole);
        return modelMapper.map(updatedProjectRole, ProjectRolesResponseDto.class);
    }

    public void deleteProjectRole(UUID id) {
        ProjectRoles projectRole = findById(id);
        projectRole.setDeleted(true);
        save(projectRole);
    }

    public ProjectRoles save(ProjectRoles projectRole) {
        return projectRolesRepository.saveAndFlush(projectRole);
    }

    public ProjectRoles findById(UUID id) {
        return projectRolesRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Project role not found"));
    }
}
