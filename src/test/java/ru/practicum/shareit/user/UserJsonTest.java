package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.dto.UserDTO;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class UserJsonTest {

    @Autowired
    private JacksonTester<UserDTO> json;


    @Test
    void validationTest_shouldThrowException_nameInvalid_emailInvalid() {
        UserDTO user = new UserDTO(1, "   ", "invalid email");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(user);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("name")));
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("email")));
    }

    @Test
    void deserialize_shouldReturnUserDTO_allValid() throws IOException {
        UserDTO expectedUser = new UserDTO(1, "valid name", "valid@email.com");
        String jsonInput = "{\"id\":1,\"name\":\"valid name\",\"email\":\"valid@email.com\"}";

        UserDTO returnedUser = json.parse(jsonInput).getObject();

        assertEquals(expectedUser, returnedUser);
    }
}
