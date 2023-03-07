package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {

    @PostMapping
    User create () {
        return null;
    }

    @GetMapping
    User getById() {
        return null;
    }

    @GetMapping
    List<User> getAll() {
        return null;
    }

    @DeleteMapping
    void delete() {

    }
}
