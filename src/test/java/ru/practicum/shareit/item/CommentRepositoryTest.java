package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.dto.CommentDTO;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class CommentRepositoryTest {

    private static final String COMMENT_TEXT = "comment text";
    private static final int ITEM_ID = 123;
    private static final int AUTHOR_ID = 456;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    private User author;
    private Comment comment;
    private CommentDTO commentDTO;
    private List<CommentDTO> returnedComments;

    @BeforeEach
    void setUp() {
        comment = new Comment();
        comment.setText(COMMENT_TEXT);
        comment.setItem(ITEM_ID);
        comment.setCreated(LocalDateTime.now());
    }

    @Test
    @DisplayName("Find comment by item all valid")
    void findCommentByItem_shouldReturnListOfComments_allValid() {
        author = userRepository.save(new User(AUTHOR_ID, "author_name", "valid@email.com"));
        comment.setAuthor(author.getId());
        commentDTO = CommentMapper.toCommentDTO(commentRepository.save(comment));

        commentDTO.setAuthorName(author.getName());
        commentDTO.setCreated(null);

        returnedComments = commentRepository.findCommentByItem(ITEM_ID);
        returnedComments.get(0).setCreated(null);

        assertEquals(returnedComments.size(), 1);
        assertEquals(returnedComments.get(0), commentDTO);
    }
}
