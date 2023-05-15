package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class ItemJsonTest {

    @Autowired
    private JacksonTester<ItemDTO> json;


    @Test
    void validationTest_shouldThrowException_nameInvalid_emailInvalid() {
        ItemDTO item = new ItemDTO();

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ItemDTO>> violations = validator.validate(item);

        assertEquals(3, violations.size());
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("name")));
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("description")));
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("available")));
    }

    @Test
    void deserialize_shouldReturnUserDTO_allValid() throws IOException {
        ItemDTO expectedItem = new ItemDTO();
        expectedItem.setId(1);
        expectedItem.setName("item name");
        expectedItem.setDescription("item description");
        expectedItem.setAvailable(true);
        expectedItem.setRequestId(2);
        String jsonInput = "{\"id\":1,\"name\":\"item name\",\"description\":\"item description\"," +
                "\"available\":true, \"requestId\":2}";

        ItemDTO returnedUser = json.parse(jsonInput).getObject();

        assertEquals(expectedItem, returnedUser);
    }
}
