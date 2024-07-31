package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Tasks;
import com.EmployeeTracking.domain.request.TasksRequestDto;
import com.EmployeeTracking.domain.response.TasksResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.TasksRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TasksService {

    private final TasksRepository tasksRepository;
    private final ProjectsService projectsService;
    private final ModelMapper modelMapper;

    public List<TasksResponseDto> getAllTasks() {
        List<Tasks> tasks = tasksRepository.findAll();
        return tasks.stream()
                .map(task -> modelMapper.map(task, TasksResponseDto.class))
                .collect(Collectors.toList());
    }

    public TasksResponseDto getTaskById(UUID id) {
        Tasks task = findById(id);
        return modelMapper.map(task, TasksResponseDto.class);
    }

    public TasksResponseDto saveTask(TasksRequestDto tasksRequestDto) {
        Projects project = projectsService.findById(tasksRequestDto.getProjectUUID());

        Tasks task = modelMapper.map(tasksRequestDto, Tasks.class);
        task.setProject(project);

        Tasks savedTask = save(task);
        return modelMapper.map(savedTask, TasksResponseDto.class);
    }

    public TasksResponseDto updateTask(UUID id, TasksRequestDto tasksRequestDto) {
        Tasks existingTask = findById(id);
        Projects project = projectsService.findById(tasksRequestDto.getProjectUUID());

        // Map the updated fields from the DTO to the existing task
        existingTask.setName(tasksRequestDto.getName());
        existingTask.setStatus(tasksRequestDto.getStatus());
        existingTask.setDescription(tasksRequestDto.getDescription());
        existingTask.setStartDate(tasksRequestDto.getStartDate());
        existingTask.setDeadline(tasksRequestDto.getDeadline());
        existingTask.setFinishDate(tasksRequestDto.getFinishDate());
        existingTask.setProject(project);

        Tasks updatedTask = save(existingTask);
        return modelMapper.map(updatedTask, TasksResponseDto.class);
    }

    public void deleteTask(UUID id) {
        Tasks task = findById(id);
        task.setDeleted(true);
        save(task);
    }

    public Tasks save(Tasks task) {
        return tasksRepository.saveAndFlush(task);
    }

    public Tasks findById(UUID id) {
        return tasksRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Task not found"));
    }
}
