package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private UserService userService;

    @PostMapping
    public UserDTO create (@Valid @RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable @Positive int id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive int id) {
        userService.delete(id);
    }
}
