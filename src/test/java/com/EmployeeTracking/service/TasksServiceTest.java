package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Projects;
import com.EmployeeTracking.domain.model.Tasks;
import com.EmployeeTracking.domain.request.TasksRequestDto;
import com.EmployeeTracking.domain.response.TasksResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.TasksRepository;
import com.EmployeeTracking.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class TasksServiceTest {

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private ProjectsService projectsService;

    @InjectMocks
    private TasksService tasksService;

    private Tasks task;
    private TasksRequestDto taskRequestDto;
    private Projects project;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = TestDataFactory.createProject(TestDataFactory.createStatus());
        task = TestDataFactory.createTask(project);
        taskRequestDto = TestDataFactory.createTasksRequestDto();
    }

    @Test
    void testGetAllTasks() {
        when(tasksRepository.findAll()).thenReturn(List.of(task));

        List<TasksResponseDto> response = tasksService.getAllTasks();

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(task.getName(), response.get(0).getName());

        verify(tasksRepository, times(1)).findAll();
    }

    @Test
    void testGetTaskById() {
        UUID id = task.getTaskId();
        when(tasksRepository.findById(id)).thenReturn(Optional.of(task));

        TasksResponseDto response = tasksService.getTaskById(id);

        assertNotNull(response);
        assertEquals(task.getTaskId(), response.getTaskId());
        assertEquals(task.getName(), response.getName());

        verify(tasksRepository, times(1)).findById(id);
    }

    @Test
    void testSaveTaskSuccess() {
        when(projectsService.findById(taskRequestDto.getProjectId())).thenReturn(project);
        when(tasksRepository.saveAndFlush(any(Tasks.class))).thenReturn(task);

        TasksResponseDto response = tasksService.saveTask(taskRequestDto);
        
        TasksResponseDto expectedResponse = ObjectMapperUtils.map(task, TasksResponseDto.class);

        assertNotNull(response, "Response should not be null");
        assertEquals(expectedResponse.getTaskId(), response.getTaskId(), "Task ID does not match");
        assertEquals(expectedResponse.getName(), response.getName(), "Task Name does not match");
        assertEquals(expectedResponse.getStatus(), response.getStatus(), "Status does not match");
        assertEquals(expectedResponse.getDescription(), response.getDescription(), "Description does not match");
        assertEquals(expectedResponse.getStartDate(), response.getStartDate(), "Start Date does not match");
        assertEquals(expectedResponse.getDeadline(), response.getDeadline(), "Deadline does not match");
        assertEquals(expectedResponse.getFinishDate(), response.getFinishDate(), "Finish Date does not match");
        assertEquals(expectedResponse.getProject(), response.getProject(), "Project does not match");
        assertEquals(expectedResponse.getCreatedAt(), response.getCreatedAt(), "Created At does not match");
        assertEquals(expectedResponse.getUpdatedAt(), response.getUpdatedAt(), "Updated At does not match");
        assertEquals(expectedResponse.getCreatedBy(), response.getCreatedBy(), "Created By does not match");
        assertEquals(expectedResponse.getUpdatedBy(), response.getUpdatedBy(), "Updated By does not match");

        verify(projectsService, times(1)).findById(taskRequestDto.getProjectId());
        verify(tasksRepository, times(1)).saveAndFlush(any(Tasks.class));
    }


    @Test
    void testUpdateTask() {
        UUID id = task.getTaskId();
        when(tasksRepository.findById(id)).thenReturn(Optional.of(task));
        when(projectsService.findById(taskRequestDto.getProjectId())).thenReturn(project);
        when(tasksRepository.saveAndFlush(any(Tasks.class))).thenReturn(task);

        TasksResponseDto response = tasksService.updateTask(id, taskRequestDto);

        assertNotNull(response);
        assertEquals(task.getTaskId(), response.getTaskId());
        assertEquals(task.getName(), response.getName());

        verify(tasksRepository, times(1)).findById(id);
        verify(projectsService, times(1)).findById(taskRequestDto.getProjectId());
        verify(tasksRepository, times(1)).saveAndFlush(any(Tasks.class));
    }

    @Test
    void testDeleteTask() {
        UUID id = task.getTaskId();
        when(tasksRepository.findById(id)).thenReturn(Optional.of(task));
        when(tasksRepository.saveAndFlush(any(Tasks.class))).thenReturn(task);

        tasksService.deleteTask(id);

        verify(tasksRepository, times(1)).findById(id);
        verify(tasksRepository, times(1)).saveAndFlush(any(Tasks.class));
    }

    @Test
    void testFindById_taskNotFound() {
        UUID id = UUID.randomUUID();
        when(tasksRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> tasksService.findById(id));

        verify(tasksRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllTasksWhenNoTasksExist() {
        when(tasksRepository.findAll()).thenReturn(Collections.emptyList());

        List<TasksResponseDto> response = tasksService.getAllTasks();

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }
}
