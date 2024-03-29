package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbiddenException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.UnknownStatusException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDTO create(Booking booking) {
        userRepository.getUserById(booking.getBookerId());
        Item item = itemRepository.getItemById(booking.getItemId());
        if (item.getOwnerId() == booking.getBookerId())
            throw new ElementNotFoundException();
        if (!item.getAvailable() || !booking.getStart().isBefore(booking.getEnd()))
            throw new BadRequestException();
        booking.setStatus(BookingStatus.WAITING);
        return BookingMapper.toBookingDTO(bookingRepository.save(booking), ItemMapper.toItemShort(item));
    }

    @Override
    public BookingDTO acceptBooking(int id, int ownerId, boolean approved) {
        Booking booking = bookingRepository.getBookingById(id);
        Item item = itemRepository.getItemById(booking.getItemId());
        if (item.getOwnerId() != ownerId)
            throw new AccessForbiddenException();
        if (booking.getStatus().equals(BookingStatus.APPROVED) && approved)
            throw new BadRequestException();
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDTO(bookingRepository.save(booking), ItemMapper.toItemShort(item));
    }

    @Override
    public BookingDTO getById(int bookingId, int userId) {
        userRepository.getUserById(userId);
        Booking booking = bookingRepository.getBookingById(bookingId);
        Item item = itemRepository.getItemById(booking.getItemId());
        if (userId != booking.getBookerId() && userId != item.getOwnerId())
            throw new AccessForbiddenException();
        return BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));
    }

    @Override
    public List<BookingDTO> getByBookerId(int userId, String state, int from, int size) {
        userRepository.getUserById(userId);
        Pageable page = PageRequest.of(from / size, size, Sort.by("start").descending());
        switch (state) {
            case "ALL":
                return bookingRepository.getAllByBookerId(userId, page);
            case "CURRENT":
                return bookingRepository.getCurrentByBookerId(userId, page);
            case "PAST":
                return bookingRepository.getPastByBookerId(userId, page);
            case "FUTURE":
                return bookingRepository.getFutureByBookerId(userId, page);
            case "WAITING":
            case "REJECTED":
                return bookingRepository.getByStatusAndBookerId(userId, BookingStatus.valueOf(state), page);
            default:
                throw new UnknownStatusException(String.format("Unknown state: %s", state));
        }
    }

    @Override
    public List<BookingDTO> getByOwnerId(int ownerId, String state, int from, int size) {
        userRepository.getUserById(ownerId);
        Pageable page = PageRequest.of(from / size, size, Sort.by("start").descending());
        switch (state) {
            case "ALL":
                return bookingRepository.getAllByOwnerId(ownerId, page);
            case "CURRENT":
                return bookingRepository.getCurrentByOwnerId(ownerId, page);
            case "PAST":
                return bookingRepository.getPastByOwnerId(ownerId, page);
            case "FUTURE":
                return bookingRepository.getFutureByOwnerId(ownerId, page);
            case "WAITING":
            case "REJECTED":
                return bookingRepository.getByStatusAndOwnerId(ownerId, BookingStatus.valueOf(state), page);
            default:
                throw new UnknownStatusException(String.format("Unknown state: %s", state));
        }
    }
}
