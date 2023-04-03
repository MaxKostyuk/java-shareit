package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingReviewStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbiddenException;
import ru.practicum.shareit.exception.BadRequestException;
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
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDTO(bookingRepository.save(booking), ItemMapper.toItemShort(item));
    }

    @Override
    public BookingDTO getById(int bookingId, int userId) {
        Booking booking = bookingRepository.getBookingById(bookingId);
        Item item = itemRepository.getItemById(booking.getItemId());
        if (userId != booking.getBookerId() && userId != item.getOwnerId())
            throw new AccessForbiddenException();
        return BookingMapper.toBookingDTO(booking, ItemMapper.toItemShort(item));
    }

    @Override
    public List<BookingDTO> getByBookerId(int userId, BookingReviewStatus state) {
        switch (state) {
            case ALL:
                return bookingRepository.getAllByBookerId(userId);
            case CURRENT:
                break;
            case PAST:
                break;
            case FUTURE:
                break;
            case WAITING:
                break;
            case REJECTED:
                break;
        }
        return null;
    }

    @Override
    public List<BookingDTO> getByOwnerId(int userId, BookingReviewStatus state) {
        return null;
    }
}
