package com.example.TicketsService.controller;

import com.example.TicketsService.dto.request.CommentRequest;
import com.example.TicketsService.dto.response.MessageResponse;
import com.example.TicketsService.model.*;
import com.example.TicketsService.security.service.UserDetailsImpl;
import com.example.TicketsService.service.CommentService;
import com.example.TicketsService.service.ConsumerService;
import com.example.TicketsService.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Tag(name="Consumer Controller", description = "Kontroler służący do obsługi żądań związanych z użytkownikami")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {

    private final ConsumerService consumerService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public ConsumerController(ConsumerService consumerService, UserService userService, CommentService commentService) {
        this.consumerService = consumerService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @Operation(summary = "Historia biletów użytkownika", description="Wyświetla historię biletów użytkownika. Tylko dla użytkowników z rolą ROLE_CONSUMER")
    @PreAuthorize("hasRole('ROLE_CONSUMER')")
    @PostMapping("/ticketHistory")
    public ResponseEntity<?> getTicketHistory(){
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userEmail = userDetails.getEmail();

            List<PurchaseEntity> purchases = consumerService.getTicketHistory(userEmail);
            return ResponseEntity.ok(purchases);
    }

    @Operation(summary = "Pobierz wszystkie bilety użytkownika o określonym id", description="Wyświetla listę biletów użytkownika po podaniu jego id. Tylko dla administracji.",
            parameters = {
            @Parameter(name = "userId", description = "Identyfikator użytkownika", required = true, example = "65b2d492a162224d2a3e957e")
    })
    @PreAuthorize("hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    @GetMapping("/ticketHistory/{userId}")
    public ResponseEntity<List<PurchaseEntity>> getTicketById(@PathVariable String userId){
        Optional<UserEntity> user = userService.getUserByUserId(new ObjectId(userId));
        List<PurchaseEntity> purchases = consumerService.getTicketHistory(user.get().getEmail());
        return ResponseEntity.ok(purchases);
    }

    //TODO dodać sprawdzanie czy użytkownik zakupił bilet na ostatnie wydarzenie tego artysty
    @Operation(summary = "Dodaj komentarz", description="Pozwala dodać nowy komentarz do konkretnego artysty, dostępne dla zalogowanych użytkowników z rolą ROLE_CONSUMER")
    @PreAuthorize("hasRole('ROLE_CONSUMER')")
    @PostMapping("/addComment")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest commentRequest){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = userDetails.getId();
        ConsumerEntity consumer = consumerService.getConsumerByUserId(userId);
        ObjectId consumerId = consumer.getId();
        CommentEntity comment = commentService.addComment(commentRequest, consumerId);

        if(comment != null){
            return ResponseEntity.ok(new MessageResponse("Comment created successfully!"));
        } else {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("Error while adding comment"));
        }
    }


    @Operation(summary = "Usuń komentarz", description = "Pozwala usunąć swój komentarz, dostępne dla zalogowanych użytkowników z rolą ROLE_CONSUMER")
    @PreAuthorize("hasRole('ROLE_CONSUMER')")
    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable String commentId){
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ObjectId userId = userDetails.getId();

        ConsumerEntity consumer = consumerService.getConsumerByUserId(userId);
        ObjectId consumerId = consumer.getId();

        try {
            commentService.deleteComment(new ObjectId(commentId), consumerId);
            return ResponseEntity.ok(new MessageResponse("Comment deleted successfully!"));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
        }


    }

}
