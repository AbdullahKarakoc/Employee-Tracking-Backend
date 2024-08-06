package com.EmployeeTracking.service;

import com.EmployeeTracking.auth.user.service.EmployeeService;
import com.EmployeeTracking.auth.user.domain.model.Employee;
import com.EmployeeTracking.domain.model.Comments;
import com.EmployeeTracking.domain.model.Tasks;
import com.EmployeeTracking.domain.request.CommentsRequestDto;
import com.EmployeeTracking.domain.response.CommentsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.CommentsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final EmployeeService employeeService;
    private final TasksService tasksService;
    private final ModelMapper modelMapper;

    public List<CommentsResponseDto> getAllComments() {
        List<Comments> comments = commentsRepository.findAll();
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentsResponseDto.class))
                .collect(Collectors.toList());
    }

    public CommentsResponseDto getCommentById(UUID id) {
        Comments comment = findById(id);
        return modelMapper.map(comment, CommentsResponseDto.class);
    }

    public CommentsResponseDto saveComment(CommentsRequestDto commentsRequestDto) {
        Employee employee = employeeService.findById(commentsRequestDto.getEmployeeId());
        Tasks task = tasksService.findById(commentsRequestDto.getTaskId());

        Comments comment = modelMapper.map(commentsRequestDto, Comments.class);
        comment.setEmployee(employee);
        comment.setTask(task);

        Comments savedComment = save(comment);
        return modelMapper.map(savedComment, CommentsResponseDto.class);
    }

    public CommentsResponseDto updateComment(UUID id, CommentsRequestDto commentsRequestDto) {
        Comments existingComment = findById(id);
        Employee employee = employeeService.findById(commentsRequestDto.getEmployeeId());
        Tasks task = tasksService.findById(commentsRequestDto.getTaskId());

        modelMapper.map(commentsRequestDto, existingComment);
        existingComment.setEmployee(employee);
        existingComment.setTask(task);

        Comments updatedComment = save(existingComment);
        return modelMapper.map(updatedComment, CommentsResponseDto.class);
    }

    public void deleteComment(UUID id) {
        Comments comment = findById(id);
        comment.setDeleted(true);
        save(comment);
    }

    public Comments save(Comments comment) {
        return commentsRepository.saveAndFlush(comment);
    }

    public Comments findById(UUID id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Comment not found"));
    }
}
