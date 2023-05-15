package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class BookingJsonTest {

    @Autowired
    private JacksonTester<Booking> json;

    @Test
    void validationTest_shouldReturn3Violations_invalidStart_invalidEnd_invalidItem() {
        Booking booking = new Booking();
        booking.setId(1);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());
        booking.setItemId(-2);
        booking.setBookerId(3);
        booking.setStatus(BookingStatus.WAITING);

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        assertEquals(3, violations.size());
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("start")));
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("end")));
        assertTrue(violations.stream().map(ConstraintViolation::getPropertyPath).anyMatch(e -> e.toString().equals("itemId")));
    }

    @Test
    void deserialize_shouldReturnBooking_allValid() throws IOException {
        Booking expectedBooking = new Booking();
        expectedBooking.setId(1);
        expectedBooking.setStart(LocalDateTime.of(3023, 12, 12, 12, 12));
        expectedBooking.setEnd(LocalDateTime.of(4023, 12, 12, 12, 12));
        expectedBooking.setItemId(2);
        expectedBooking.setBookerId(3);
        expectedBooking.setStatus(BookingStatus.WAITING);

        String jsonInput = "{\"id\":1,\"start\":\"3023-12-12T12:12:00\",\"end\":\"4023-12-12T12:12:00\"," +
                "\"itemId\":2,\"bookerId\":3,\"status\":\"WAITING\"}";

        Booking returnedBooking = json.parse(jsonInput).getObject();

        assertEquals(expectedBooking, returnedBooking);

    }
}
