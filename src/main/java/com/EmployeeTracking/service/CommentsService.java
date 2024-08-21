package com.EmployeeTracking.service;

import com.EmployeeTracking.config.modelMapper.ObjectMapperUtils;
import com.EmployeeTracking.domain.model.Employee;
import com.EmployeeTracking.domain.model.Comments;
import com.EmployeeTracking.domain.model.Tasks;
import com.EmployeeTracking.domain.request.CommentsRequestDto;
import com.EmployeeTracking.domain.response.CommentsResponseDto;
import com.EmployeeTracking.exception.DataNotFoundException;
import com.EmployeeTracking.repository.CommentsRepository;
import com.EmployeeTracking.util.AppUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final EmployeeService employeeService;
    private final TasksService tasksService;

    public List<CommentsResponseDto> getAllComments() {
        List<Comments> comments = commentsRepository.findAll();

        if (comments.isEmpty()) {
            return AppUtils.emptyList();
        }

        return ObjectMapperUtils.mapAll(comments, CommentsResponseDto.class);
    }

    public CommentsResponseDto getCommentById(UUID id) {
        Comments comment = findById(id);
        return ObjectMapperUtils.map(comment, CommentsResponseDto.class);
    }

    @Transactional
    public CommentsResponseDto saveComment(CommentsRequestDto commentsRequestDto) {
        Employee employee = employeeService.findById(commentsRequestDto.getEmployeeId());
        Tasks task = tasksService.findById(commentsRequestDto.getTaskId());

        Comments comment = ObjectMapperUtils.map(commentsRequestDto, Comments.class);
        comment.setEmployee(employee);
        comment.setTask(task);

        Comments savedComment = save(comment);
        return ObjectMapperUtils.map(savedComment, CommentsResponseDto.class);
    }

    @Transactional
    public CommentsResponseDto updateComment(UUID id, CommentsRequestDto commentsRequestDto) {
        Comments existingComment = findById(id);
        Employee employee = employeeService.findById(commentsRequestDto.getEmployeeId());
        Tasks task = tasksService.findById(commentsRequestDto.getTaskId());

        ObjectMapperUtils.map(commentsRequestDto, existingComment);
        existingComment.setEmployee(employee);
        existingComment.setTask(task);

        Comments updatedComment = save(existingComment);
        return ObjectMapperUtils.map(updatedComment, CommentsResponseDto.class);
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
