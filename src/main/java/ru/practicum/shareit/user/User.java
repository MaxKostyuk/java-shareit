package ru.practicum.shareit.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
public class User {

    private int id;
    @NotBlank
    private String name;
    @Email
    private String email;
}
