package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    @InjectMocks
    private CommentMapper mapper;

    @Test
    void toComment() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setText("text");

        Comment comment = new Comment();
        comment.setText("text");

        assertEquals(comment, mapper.toComment(commentRequest));
    }

    @Test
    void toCommentResponse() {
        Comment comment = new Comment();
        comment.setId(1);
        comment.setAuthor(new User(1, "name", null));
        comment.setText("text");
        comment.setCreated(LocalDateTime.now());

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(1);
        commentResponse.setAuthorName("name");
        commentResponse.setText("text");
        commentResponse.setCreated(comment.getCreated());

        assertEquals(commentResponse, mapper.toCommentResponse(comment));
    }
}