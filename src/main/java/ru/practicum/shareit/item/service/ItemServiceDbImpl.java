package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbiddenException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDTO;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceDbImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public ItemDTO create(ItemDTO itemDTO, int userId) {
        userRepository.getUserById(userId);
        Item item = ItemMapper.toItem(itemDTO);
        item.setOwnerId(userId);
        return ItemMapper.toItemDTO(itemRepository.save(item));
    }

    @Override
    public ItemDTO getById(int id, int userId) {
        Item item = itemRepository.getItemById(id);
        ItemDTO itemDTO = ItemMapper.toItemDTO(item);
        if (item.getOwnerId() == userId)
            setBookingDates(itemDTO);
        itemDTO.setComments(commentRepository.findCommentByItem(itemDTO.getId()));
        return itemDTO;
    }

    @Override
    public List<ItemDTO> getByUser(int userId) {
        userRepository.getUserById(userId);
        return itemRepository.findByOwnerId(userId).stream()
                .map(ItemMapper::toItemDTO)
                .peek(this::setBookingDates)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDTO> search(String text) {
        return itemRepository.search(text)
                .stream().map(ItemMapper::toItemDTO).collect(Collectors.toList());
    }

    @Override
    public ItemDTO update(ItemDTO itemDTO, int userId) {
        Item itemToUpdate = itemRepository.getItemById(itemDTO.getId());
        if (itemToUpdate.getOwnerId() != userId)
            throw new AccessForbiddenException();
        if (Objects.nonNull(itemDTO.getName()))
            itemToUpdate.setName(itemDTO.getName());
        if (Objects.nonNull(itemDTO.getDescription()))
            itemToUpdate.setDescription(itemDTO.getDescription());
        if (Objects.nonNull(itemDTO.getAvailable()))
            itemToUpdate.setAvailable(itemDTO.getAvailable());
        return ItemMapper.toItemDTO(itemRepository.save(itemToUpdate));
    }

    @Override
    public CommentDTO addComment(Comment comment) {
        User user = userRepository.getUserById(comment.getAuthor());
        itemRepository.getItemById(comment.getItem());
        if (!bookingRepository.checkBookingForComments(comment.getAuthor(), comment.getItem()))
            throw new BadRequestException();
        CommentDTO commentDTO = CommentMapper.toCommentDTO(commentRepository.save(comment));
        commentDTO.setAuthorName(user.getName());
        return commentDTO;
    }

    private void setBookingDates(ItemDTO itemDTO) {
        Optional<Booking> lastBooking = bookingRepository.findTopByItemIdAndStartBeforeOrderByEndDesc(itemDTO.getId(), LocalDateTime.now());
        itemDTO.setLastBooking(lastBooking.map(BookingMapper::toBookingShortDTO).orElse(null));
        Optional<Booking> nextBooking = bookingRepository.findTopByItemIdAndStartAfterAndStatusNotOrderByStartAsc(itemDTO.getId(), LocalDateTime.now(), BookingStatus.REJECTED);
        itemDTO.setNextBooking(nextBooking.map(BookingMapper::toBookingShortDTO).orElse(null));
    }
}
