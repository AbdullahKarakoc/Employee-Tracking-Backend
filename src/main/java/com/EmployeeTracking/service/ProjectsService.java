package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Status;
import com.EmployeeTracking.domain.request.ProjectsRequestDto;
import com.EmployeeTracking.domain.response.ProjectsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.ProjectsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectsService {

    private final ProjectsRepository projectsRepository;
    private final ModelMapper modelMapper;

    public List<ProjectsResponseDto> getAllProjects() {
        List<Projects> projects = projectsRepository.findAll();
        return projects.stream()
                .map(project -> modelMapper.map(project, ProjectsResponseDto.class))
                .toList();
    }

    public ProjectsResponseDto getProjectById(UUID id) {
        Projects project = findById(id);
        return modelMapper.map(project, ProjectsResponseDto.class);
    }

    public ProjectsResponseDto saveProject(ProjectsRequestDto projectRequestDto) {
        Projects project = modelMapper.map(projectRequestDto, Projects.class);
        project.setStatus(modelMapper.map(projectRequestDto.getStatus(), Status.class));

        Projects savedProject = save(project);
        return modelMapper.map(savedProject, ProjectsResponseDto.class);
    }

    public ProjectsResponseDto updateProject(UUID id, ProjectsRequestDto projectRequestDto) {
        Projects existingProject = findById(id);
        modelMapper.map(projectRequestDto, existingProject);
        existingProject.setStatus(modelMapper.map(projectRequestDto.getStatus(), Status.class));

        Projects updatedProject = save(existingProject);
        return modelMapper.map(updatedProject, ProjectsResponseDto.class);
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


