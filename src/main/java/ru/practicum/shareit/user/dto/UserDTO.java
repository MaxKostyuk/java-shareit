package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class UserDTO {

    private int id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

}
