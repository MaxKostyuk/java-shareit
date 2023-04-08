package ru.practicum.shareit.booking.storage;

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
            "where bk.bookerId = ?1 " +
            "order by bk.start desc")
    List<BookingDTO> getAllByBookerId(int bookerId);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and bk.start < ?2 " +
            "and bk.end > ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getCurrentByBookerId(int bookerId, LocalDateTime time);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and bk.end < ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getPastByBookerId(int bookerId, LocalDateTime time);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and bk.start > ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getFutureByBookerId(int bookerId, LocalDateTime time);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "and bk.status = ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getByStatusAndBookerId(int bookerId, BookingStatus status);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "order by bk.start desc")
    List<BookingDTO> getAllByOwnerId(int ownerId);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and bk.start < ?2 " +
            "and bk.end > ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getCurrentByOwnerId(int ownerId, LocalDateTime time);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and bk.end < ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getPastByOwnerId(int ownerId, LocalDateTime time);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and bk.start > ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getFutureByOwnerId(int ownerId, LocalDateTime time);

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where it.ownerId = ?1 " +
            "and bk.status = ?2 " +
            "order by bk.start desc")
    List<BookingDTO> getByStatusAndOwnerId(int ownerId, BookingStatus status);

    Optional<Booking> findTopByItemIdAndStartAfterAndStatusNotOrderByStartAsc(int itemId, LocalDateTime time, BookingStatus status);

    Optional<Booking> findTopByItemIdAndEndBeforeOrderByEndDesc(int itemId, LocalDateTime time);
}
