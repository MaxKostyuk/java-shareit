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

    private final UserService userService;

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

    @PatchMapping("/{id}")
    public UserDTO update(@RequestBody UserDTO userDTO, @PathVariable @Positive int id) {
        userDTO.setId(id);
        return userService.update(userDTO);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable @Positive int id) {
        userService.delete(id);
    }
}
