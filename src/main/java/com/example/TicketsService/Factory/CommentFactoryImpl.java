package com.example.TicketsService.Factory;

import com.example.TicketsService.dto.request.CommentRequest;
import com.example.TicketsService.model.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentFactoryImpl implements CommentFactory{
    @Override
    public CommentEntity createComment(String consumerId, CommentRequest commentRequest){
        return new CommentEntity(commentRequest.getArtistId(), consumerId, commentRequest.getStars(), commentRequest.getDescription());
    }
}
