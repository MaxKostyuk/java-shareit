package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookerDTO;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.dto.BookingResponseDTO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingReviewStatus;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.AccessForbiddenException;
import ru.practicum.shareit.exception.ElementNotFoundException;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemShort;
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
    public BookingResponseDTO create(BookingDTO bookingDTO) {
        userRepository.findById(bookingDTO.getBookerId())
                .orElseThrow(() -> new ElementNotFoundException(String.format(USER_NOT_FOUND_TEMPLATE, bookingDTO.getBookerId())));
        Item item = itemRepository.findById(bookingDTO.getItemId())
                .orElseThrow(() -> new ElementNotFoundException(String.format(BOOKING_NOT_FOUND_TEMPLATE, bookingDTO.getItemId())));
        if (!item.getAvailable() || !bookingDTO.getStart().isBefore(bookingDTO.getEnd()))
            throw new BadRequestException();
        bookingDTO.setStatus(BookingStatus.WAITING);
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDTO));
        BookingResponseDTO bookingResponseDTO = BookingMapper.bookingResponseDTO(booking);
        bookingResponseDTO.setItem(itemRepository.getShortById(booking.getItem()));
        return bookingResponseDTO;
    }

    @Override
    public BookingResponseDTO acceptBooking(int id, int ownerId, boolean approved) {
        Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new ElementNotFoundException(String.format(BOOKING_NOT_FOUND_TEMPLATE, id)));
        Item item = itemRepository.findById(booking.getItem()).get();
        if(item.getOwnerId() != ownerId)
            throw new AccessForbiddenException();
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        BookingResponseDTO bookingResponseDTO = BookingMapper.bookingResponseDTO(bookingRepository.save(booking));
        bookingResponseDTO.setBooker(new BookerDTO(booking.getBookerId()));
        bookingResponseDTO.setItem(itemRepository.getShortById(booking.getItem()));
        return bookingResponseDTO;
    }

    @Override
    public List<BookingResponseDTO> getById(int bookingId) {
        return null;
    }

    @Override
    public List<BookingResponseDTO> getByBookerId(int bookerId, BookingReviewStatus state) {
        return null;
    }

    @Override
    public List<BookingResponseDTO> getByOwnerId(int ownerId, BookingReviewStatus state) {
        return null;
    }
}
