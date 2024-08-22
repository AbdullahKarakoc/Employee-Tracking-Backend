package com.EmployeeTracking.controller;

import com.EmployeeTracking.domain.request.CommentsRequestDto;
import com.EmployeeTracking.domain.response.CommentsResponseDto;
import com.EmployeeTracking.service.CommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
@Tag(name = "Comments-Controller", description = "Controller managing operations related to comments")
@SecurityRequirement(name = "bearerAuth")
public class CommentsController {

    private final CommentsService commentsService;

    @Operation(
            summary = "Save a new comment",
            description = "An endpoint used to save a new comment.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully saved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PostMapping
    public ResponseEntity<CommentsResponseDto> saveComment(@Valid @RequestBody CommentsRequestDto commentRequestDto) {
        CommentsResponseDto savedComment = commentsService.saveComment(commentRequestDto);
        return ResponseEntity.ok(savedComment);
    }

    @Operation(
            summary = "Get all comments",
            description = "An endpoint used to list all comments.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping
    public ResponseEntity<List<CommentsResponseDto>> getAllComments() {
        List<CommentsResponseDto> allComments = commentsService.getAllComments();
        return ResponseEntity.ok(allComments);
    }

    @Operation(
            summary = "Get comment by ID",
            description = "An endpoint used to get details of a comment by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved comment"),
                    @ApiResponse(responseCode = "404", description = "Comment not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<CommentsResponseDto> getCommentById(@PathVariable UUID id) {
        CommentsResponseDto comment = commentsService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }

    @Operation(
            summary = "Update comment",
            description = "An endpoint used to update an existing comment by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Comment not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<CommentsResponseDto> updateComment(@Valid @PathVariable UUID id, @RequestBody @Valid CommentsRequestDto commentRequestDto) {
        CommentsResponseDto updatedComment = commentsService.updateComment(id, commentRequestDto);
        return ResponseEntity.ok(updatedComment);
    }

    @Operation(
            summary = "Delete comment",
            description = "An endpoint used to delete an existing comment by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Comment not found"),
                    @ApiResponse(responseCode = "403", description = "Unauthorized access")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable UUID id) {
        commentsService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}