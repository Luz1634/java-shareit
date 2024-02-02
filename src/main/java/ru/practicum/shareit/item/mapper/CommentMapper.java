package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.model.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public Comment toComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        return comment;
    }

    public CommentResponse toCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setAuthorName(comment.getAuthor().getName());
        commentResponse.setText(comment.getText());
        commentResponse.setCreated(comment.getCreated());
        return commentResponse;
    }
}
