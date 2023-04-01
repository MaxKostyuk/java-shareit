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
import ru.practicum.shareit.exception.ElementNotFoundException;
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

    private static final String ITEM_NOT_FOUND_TEMPLATE = "Item with id %d not found";
    private static final String BOOKING_NOT_FOUND_TEMPLATE = "Booking with id %d not found";
    private static final String USER_NOT_FOUND_TEMPLATE = "User with id %d not found";

    @Override
    public BookingDTO create(Booking booking) {
        //вот эта строка упрощается
        userRepository.findById(booking.getBookerId())
                .orElseThrow(() -> new ElementNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, booking.getBookerId())));
        //вот эта строка упрощается
        Item item = itemRepository.findById(booking.getItemId())
                .orElseThrow(() -> new ElementNotFoundException(String.format(ITEM_NOT_FOUND_TEMPLATE, booking.getItemId())));
        if (!item.getAvailable() || !booking.getStart().isBefore(booking.getEnd()))
            throw new BadRequestException();
        booking.setStatus(BookingStatus.WAITING);
        BookingDTO bookingDTO = BookingMapper.toBookingDTO(bookingRepository.save(booking));
        //вот эта строка уходит
        bookingDTO.setItem(itemRepository.getShortById(booking.getItemId()));
        return bookingDTO;
    }

    @Override
    public BookingDTO acceptBooking(int id, int ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new ElementNotFoundException(String.format(BOOKING_NOT_FOUND_TEMPLATE, id)));
        Item item = itemRepository.findById(booking.getItemId()).get();
        if(item.getOwnerId() != ownerId)
            throw new AccessForbiddenException();
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        BookingDTO bookingDTO = BookingMapper.toBookingDTO(bookingRepository.save(booking));
        bookingDTO.setItem(itemRepository.getShortById(booking.getItemId()));
        return bookingDTO;
    }

    @Override
    public List<BookingDTO> getById(int bookingId) {
        return null;
    }

    @Override
    public List<BookingDTO> getByBookerId(int bookerId, BookingReviewStatus state) {
        return null;
    }

    @Override
    public List<BookingDTO> getByOwnerId(int ownerId, BookingReviewStatus state) {
        return null;
    }
}
