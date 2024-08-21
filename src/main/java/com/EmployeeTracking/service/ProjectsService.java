package com.EmployeeTracking.service;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectsRepository;
import com.EmployeeTracking.util.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProjectsService {

    private final ProjectsRepository projectsRepository;

    public List<ProjectsResponseDto> getAllProjects() {
        List<Projects> projects = projectsRepository.findAll();

        if (projects.isEmpty()) {
            return AppUtils.emptyList();
        }

        return ObjectMapperUtils.mapAll(projects, ProjectsResponseDto.class);
    }

    public ProjectsResponseDto getProjectById(UUID id) {
        Projects project = findById(id);
        return ObjectMapperUtils.map(project, ProjectsResponseDto.class);
    }

    public ProjectsResponseDto saveProject(ProjectsRequestDto projectRequestDto) {
        Projects project = ObjectMapperUtils.map(projectRequestDto, Projects.class);
        project.setStatus(ObjectMapperUtils.map(projectRequestDto.getStatus(), Status.class));

        Projects savedProject = save(project);
        return ObjectMapperUtils.map(savedProject, ProjectsResponseDto.class);
    }

    public ProjectsResponseDto updateProject(UUID id, ProjectsRequestDto projectRequestDto) {
        Projects existingProject = findById(id);

        ObjectMapperUtils.map(projectRequestDto, existingProject);
        existingProject.setStatus(ObjectMapperUtils.map(projectRequestDto.getStatus(), Status.class));

        Projects updatedProject = save(existingProject);
        return ObjectMapperUtils.map(updatedProject, ProjectsResponseDto.class);
    }

    public void deleteProject(UUID id) {
        Projects project = findById(id);
        project.setDeleted(true);
        save(project);
    }

    public Projects save(Projects project) {
        return projectsRepository.saveAndFlush(project);
    }

    public Projects findById(UUID id) {
        return projectsRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Project not found"));
    }
}

