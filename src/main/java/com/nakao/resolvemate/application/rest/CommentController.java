package com.nakao.resolvemate.application.rest;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.domain.comment.CommentDTO;
import com.nakao.resolvemate.domain.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{ticketId}")
    public ResponseEntity<CommentDTO> createComment(@PathVariable UUID ticketId, @RequestBody Comment comment) {
        CommentDTO createdComment = commentService.createComment(ticketId, comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId(@PathVariable UUID ticketId) {
        List<CommentDTO> comments = commentService.getCommentsByTicketId(ticketId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
