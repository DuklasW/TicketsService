package com.example.TicketsService;
import com.example.TicketsService.controller.ArtistController;
import com.example.TicketsService.Mapper.CommentMapper;
import com.example.TicketsService.dto.response.CommentResponse;
import com.example.TicketsService.model.CommentEntity;
import com.example.TicketsService.service.CommentService;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArtistControllerTest {

    @InjectMocks
    private ArtistController artistController;

    @Mock
    private CommentService commentService;

    @Mock
    private CommentMapper commentMapper;

    @SneakyThrows
    @Test
    public void testShowArtistComment_Success() {
        ObjectId comm1 = new ObjectId();
        ObjectId comm2 = new ObjectId();
        ObjectId artistId = new ObjectId();
        CommentEntity comment1 = createCommentEntity(comm1, artistId);
        CommentEntity comment2 = createCommentEntity(comm2, artistId);
        List<CommentEntity> comments = Arrays.asList(
                comment1,
                comment2
        );
        CommentResponse commentRespone1 = createCommentResponse(comm1, artistId);
        CommentResponse commentRespone2 = createCommentResponse(comm2, artistId);
        List<CommentResponse> expectedResponses = Arrays.asList(
                commentRespone1,
                commentRespone2
        );

        when(commentService.getArtistComments(artistId.toHexString())).thenReturn(comments);
        when(commentMapper.toResponse(comments.get(0))).thenReturn(expectedResponses.get(0));
        when(commentMapper.toResponse(comments.get(1))).thenReturn(expectedResponses.get(1));

        ResponseEntity<?> response = artistController.showArtistComment(artistId.toHexString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponses, response.getBody());
    }

    private CommentEntity createCommentEntity(ObjectId id, ObjectId artistId) {
        CommentEntity comment = new CommentEntity();
        comment.setId(id);
        comment.setDescription("comment");
        comment.setConsumerId("user1");
        comment.setArtistId(artistId.toHexString());
        return comment;
    }

    private CommentResponse createCommentResponse(ObjectId id, ObjectId artistId) {
        CommentResponse comment = new CommentResponse();
        comment.setDescription("comment");
        comment.setArtistId(artistId.toHexString());
        comment.setStars(3);
        comment.setId(id.toHexString());
        return comment;
    }

    @SneakyThrows
    @Test
    public void testShowArtistComment_EmptyComments() {
        String artistId = "artist123";
        List<CommentEntity> emptyComments = Arrays.asList();

        when(commentService.getArtistComments(artistId)).thenReturn(emptyComments);

        ResponseEntity<?> response = artistController.showArtistComment(artistId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(emptyComments, response.getBody());
    }

    @SneakyThrows
    @Test
    public void testShowArtistComment_InvalidArtistId() {
        String invalidArtistId = "";

        when(commentService.getArtistComments(invalidArtistId)).thenThrow(new Exception("Niepoprawny format artistId."));

        ResponseEntity<?> response = artistController.showArtistComment(invalidArtistId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Niepoprawny format artistId.", response.getBody());
    }
}
