package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.model.Comment;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class CommentJsonTest {

    @Autowired
    JacksonTester<Comment> json;

    @Test
    void validationTest_shouldThrowViolations_textInvalid() {
        Comment comment = new Comment();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Comment>> violations = validator.validate(comment);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("text")));
    }

    @Test
    void deserialization_shouldReturnComment_allValid() throws IOException {
        Comment expectedComment = new Comment();
        expectedComment.setId(1);
        expectedComment.setText("comment text");
        expectedComment.setItem(2);
        expectedComment.setAuthor(3);
        expectedComment.setCreated(LocalDateTime.of(2012, 12, 12, 12, 12));

        String jsonInput = "{\"id\":1,\"text\":\"comment text\",\"item\":2," +
                "\"author\":3,\"created\":\"2012-12-12T12:12:00\"}";
        Comment returnedComment = json.parse(jsonInput).getObject();

        assertEquals(expectedComment, returnedComment);
    }
}
