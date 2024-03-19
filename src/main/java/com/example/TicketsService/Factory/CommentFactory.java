package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.CommentRequest;
import com.example.TicketsService.model.CommentEntity;

public interface CommentFactory {
    CommentEntity createComment(String consumerId, CommentRequest commentRequest);

}
