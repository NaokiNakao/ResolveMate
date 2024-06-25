package com.nakao.resolvemate.application.rest;

import com.nakao.resolvemate.domain.comment.Comment;
import com.nakao.resolvemate.domain.comment.CommentDTO;
import com.nakao.resolvemate.domain.ticket.Ticket;
import com.nakao.resolvemate.domain.ticket.TicketDTO;
import com.nakao.resolvemate.domain.ticket.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody Ticket ticket) {
        TicketDTO createdTicket = ticketService.createTicket(ticket);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = ticketService.getAllTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable UUID id) {
        TicketDTO ticket = ticketService.getTicketById(id);
        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @PostMapping("/{ticketId}/comments")
    public ResponseEntity<CommentDTO> createComment(@PathVariable UUID ticketId, @RequestBody Comment comment) {
        CommentDTO createdComment = ticketService.createComment(ticketId, comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/{ticketId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByTicketId(@PathVariable UUID ticketId) {
        List<CommentDTO> comments = ticketService.getCommentsByTicketId(ticketId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
