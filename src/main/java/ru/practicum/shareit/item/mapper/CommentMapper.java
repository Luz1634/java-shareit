package ru.practicum.shareit.item.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDb;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.CommentResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

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

    public Comment toComment(CommentDb commentDb) {
        Comment comment = new Comment();
        comment.setId(commentDb.getId());
        comment.setItem(itemMapper.toItem(commentDb.getItem()));
        comment.setAuthor(userMapper.toUser(commentDb.getAuthor()));
        comment.setText(commentDb.getText());
        comment.setCreated(commentDb.getCreated());
        return comment;
    }

    public CommentDb toCommentDb(Comment comment, LocalDateTime created) {
        CommentDb commentDb = new CommentDb();
        commentDb.setId(comment.getId());
        commentDb.setItem(itemMapper.toItemDb(comment.getItem()));
        commentDb.setAuthor(userMapper.toUserDb(comment.getAuthor()));
        commentDb.setText(comment.getText());
        commentDb.setCreated(created);
        return commentDb;
    }
}
