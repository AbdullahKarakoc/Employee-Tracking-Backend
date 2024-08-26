package com.EmployeeTracking.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.*;
import com.EmployeeTracking.domain.request.CommentsRequestDto;
import com.EmployeeTracking.domain.response.CommentsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.CommentsRepository;
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

class CommentsServiceTest {

    @Mock
    private CommentsRepository commentsRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private TasksService tasksService;

    @InjectMocks
    private CommentsService commentsService;

    private Comments comment;
    private CommentsRequestDto commentRequestDto;
    private Employee employee;
    private Tasks task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        Status status = TestDataFactory.createStatus();
        Projects project = TestDataFactory.createProject(status);
        employee = TestDataFactory.createEmployee();
        task = TestDataFactory.createTask(project);
        comment = TestDataFactory.createComment(employee, task);
        commentRequestDto = TestDataFactory.createCommentsRequestDto();
    }

    @Test
    void testGetAllComments() {
        // Arrange
        when(commentsRepository.findAll()).thenReturn(List.of(comment));

        // Act
        List<CommentsResponseDto> response = commentsService.getAllComments();

        // Assert
        assertNotNull(response, "Response should not be null");
        assertFalse(response.isEmpty(), "Response should not be empty");
        assertEquals(1, response.size(), "Response size should be 1");
        assertEquals(comment.getTitle(), response.get(0).getTitle(), "Comment title does not match");

        verify(commentsRepository, times(1)).findAll();
    }

    @Test
    void testGetCommentById() {
        UUID id = comment.getCommentId();
        when(commentsRepository.findById(id)).thenReturn(Optional.of(comment));

        CommentsResponseDto response = commentsService.getCommentById(id);

        assertNotNull(response, "Response should not be null");
        assertEquals(comment.getCommentId(), response.getCommentId(), "Comment ID does not match");
        assertEquals(comment.getTitle(), response.getTitle(), "Comment title does not match");

        verify(commentsRepository, times(1)).findById(id);
    }

    @Test
    void testSaveCommentSuccess() {
        when(employeeService.findById(commentRequestDto.getEmployeeId())).thenReturn(employee);
        when(tasksService.findById(commentRequestDto.getTaskId())).thenReturn(task);
        when(commentsRepository.saveAndFlush(any(Comments.class))).thenReturn(comment);

        CommentsResponseDto response = commentsService.saveComment(commentRequestDto);
        CommentsResponseDto expectedResponse = ObjectMapperUtils.map(comment, CommentsResponseDto.class);

        assertNotNull(response, "Response should not be null");
        assertEquals(expectedResponse.getCommentId(), response.getCommentId(), "Comment ID does not match");
        assertEquals(expectedResponse.getTitle(), response.getTitle(), "Comment title does not match");
        assertEquals(expectedResponse.getDescription(), response.getDescription(), "Description does not match");
        assertEquals(expectedResponse.getEmployee(), response.getEmployee(), "Employee does not match");
        assertEquals(expectedResponse.getTask(), response.getTask(), "Task does not match");
        assertEquals(expectedResponse.getCreatedAt(), response.getCreatedAt(), "Created At does not match");
        assertEquals(expectedResponse.getUpdatedAt(), response.getUpdatedAt(), "Updated At does not match");
        assertEquals(expectedResponse.getCreatedBy(), response.getCreatedBy(), "Created By does not match");
        assertEquals(expectedResponse.getUpdatedBy(), response.getUpdatedBy(), "Updated By does not match");

        verify(employeeService, times(1)).findById(commentRequestDto.getEmployeeId());
        verify(tasksService, times(1)).findById(commentRequestDto.getTaskId());
        verify(commentsRepository, times(1)).saveAndFlush(any(Comments.class));
    }


    @Test
    void testUpdateComment() {
        UUID id = comment.getCommentId();
        when(commentsRepository.findById(id)).thenReturn(Optional.of(comment));
        when(employeeService.findById(commentRequestDto.getEmployeeId())).thenReturn(employee);
        when(tasksService.findById(commentRequestDto.getTaskId())).thenReturn(task);
        when(commentsRepository.saveAndFlush(any(Comments.class))).thenReturn(comment);

        CommentsResponseDto response = commentsService.updateComment(id, commentRequestDto);

        assertNotNull(response, "Response should not be null");
        assertEquals(comment.getCommentId(), response.getCommentId(), "Comment ID does not match");
        assertEquals(comment.getTitle(), response.getTitle(), "Comment title does not match");

        verify(commentsRepository, times(1)).findById(id);
        verify(employeeService, times(1)).findById(commentRequestDto.getEmployeeId());
        verify(tasksService, times(1)).findById(commentRequestDto.getTaskId());
        verify(commentsRepository, times(1)).saveAndFlush(any(Comments.class));
    }

    @Test
    void testDeleteComment() {
        UUID id = comment.getCommentId();
        when(commentsRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentsRepository.saveAndFlush(any(Comments.class))).thenReturn(comment);

        commentsService.deleteComment(id);

        verify(commentsRepository, times(1)).findById(id);
        verify(commentsRepository, times(1)).saveAndFlush(any(Comments.class));
    }

    @Test
    void testFindById_commentNotFound() {
        UUID id = UUID.randomUUID();
        when(commentsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> commentsService.findById(id), "Expected DataNotFoundException to be thrown");

        verify(commentsRepository, times(1)).findById(id);
    }

    @Test
    void testGetAllCommentsWhenNoCommentsExist() {
        when(commentsRepository.findAll()).thenReturn(Collections.emptyList());

        List<CommentsResponseDto> response = commentsService.getAllComments();

        assertNotNull(response, "Response should not be null");
        assertTrue(response.isEmpty(), "Response should be empty");
    }
}
