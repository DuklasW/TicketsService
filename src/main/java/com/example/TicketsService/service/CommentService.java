package com.example.TicketsService.service;

import com.example.TicketsService.dto.request.CommentRequest;
import com.example.TicketsService.model.CommentEntity;
import com.example.TicketsService.repository.CommentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public CommentEntity addComment(CommentRequest commentRequest, ObjectId consumerId) {
        CommentEntity comment = createComment(commentRequest, consumerId);
        return commentRepository.save(comment);
    }

    public CommentEntity createComment(CommentRequest commentRequest, ObjectId consumerId) {
        return new CommentEntity(commentRequest.getArtistId(), consumerId.toHexString(), commentRequest.getStars(), commentRequest.getDescription());
    }

    public List<CommentEntity> getArtistComments(String artistId) {
        return commentRepository.findByArtistId(new ObjectId(artistId));
    }

    public CommentEntity deleteComment(ObjectId objectId, ObjectId consumerId) {
        CommentEntity comment = commentRepository.findById(objectId).get();
        if (comment.getConsumerId().equals(consumerId.toHexString())) {
            commentRepository.delete(comment);
            return comment;
        } else {
            throw new RuntimeException("You are not authorized to delete this comment");
        }
    }
}
