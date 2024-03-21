package com.example.TicketsService.Mapper;

import com.example.TicketsService.Mapper.common.AbstractMapper;
import com.example.TicketsService.dto.response.CommentResponse;
import com.example.TicketsService.model.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper extends AbstractMapper<CommentEntity, CommentResponse>{
    @Override
    public CommentResponse toResponse(CommentEntity commentEntity) {
        CommentResponse response = new CommentResponse();
        response.setArtistId(commentEntity.getArtistId());
        response.setDescription(commentEntity.getDescription());
        response.setStars(commentEntity.getStars());
        response.setId(commentEntity.getId().toHexString());

        return response;
    }
}
