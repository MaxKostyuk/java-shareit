package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class RequestJsonTest {

    @Autowired
    private JacksonTester<ItemRequest> json;


    @Test
    void validationTest_shouldThrowException_descriptionInvalid() {
        ItemRequest request = new ItemRequest(1, 1, "   ", null);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ItemRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("description")));
    }

    @Test
    void deserialize_shouldReturnItemRequest_allValid() throws IOException {
        ItemRequest expectedRequest = new ItemRequest(1, 1, "item description", LocalDateTime.of(2012, 12, 12, 12, 12));
        String jsonInptut ="{\"id\":1,\"description\":\"item description\"," +
                "\"userId\":1,\"created\":\"2012-12-12T12:12:00\"}";

        ItemRequest returnedRequest = json.parse(jsonInptut).getObject();

        assertEquals(expectedRequest, returnedRequest);
    }
}
