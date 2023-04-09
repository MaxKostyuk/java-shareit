package ru.practicum.shareit.item.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "select new ru.practicum.shareit.item.comment.CommentDTO(c.id, c.text, u.name, c.created) " +
            "from Comment c " +
            "join User u on c.author = u.id " +
            "where c.item = ?1")
    List<CommentDTO> findCommentByItem(int itemId);
}
