package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceDbImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO create(UserDTO userDTO) {
        return UserMapper.toUserDTO(userRepository.save(UserMapper.toUser(userDTO)));
    }

    @Override
    public UserDTO getById(int id) {
        return UserMapper.toUserDTO(userRepository.getUserById(id));
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO update(UserDTO userDTO) {
        User userToUpdate = userRepository.getUserById(userDTO.getId());
        if (Objects.nonNull(userDTO.getName()))
            userToUpdate.setName(userDTO.getName());
        if (Objects.nonNull(userDTO.getEmail()))
            userToUpdate.setEmail(userDTO.getEmail());
        return UserMapper.toUserDTO(userRepository.save(userToUpdate));
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }

}
