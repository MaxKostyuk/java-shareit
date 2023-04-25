package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, BookingRepositoryCustom {

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1")
    List<BookingDTO> getAllByBookerId(int bookerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and current_timestamp between bk.start and bk.end")
    List<BookingDTO> getCurrentByBookerId(int bookerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and bk.end < current_timestamp")
    List<BookingDTO> getPastByBookerId(int bookerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and bk.start > current_timestamp")
    List<BookingDTO> getFutureByBookerId(int bookerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and bk.status = ?2")
    List<BookingDTO> getByStatusAndBookerId(int bookerId, BookingStatus status, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1")
    List<BookingDTO> getAllByOwnerId(int ownerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and current_timestamp between bk.start and bk.end")
    List<BookingDTO> getCurrentByOwnerId(int ownerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and bk.end < current_timestamp")
    List<BookingDTO> getPastByOwnerId(int ownerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and bk.start > current_timestamp")
    List<BookingDTO> getFutureByOwnerId(int ownerId, Pageable page);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and bk.status = ?2")
    List<BookingDTO> getByStatusAndOwnerId(int ownerId, BookingStatus status, Pageable page);

    Optional<Booking> findTopByItemIdAndStartAfterAndStatusNotOrderByStartAsc(int itemId, LocalDateTime time, BookingStatus status);

    Optional<Booking> findTopByItemIdAndStartBeforeOrderByEndDesc(int itemId, LocalDateTime time);

    @Query(value = "select b from Booking b " +
            "where b.bookerId = ?1 " +
            "and b.itemId = ?2 " +
            "and b.end < current_timestamp")
    List<Booking> getPastBookingsForUserAndItem(int userId, int itemId);
}
