package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
public class UserDTO {

    private int id;
    @NotBlank
    private String name;
    @Email
    private String email;

}
