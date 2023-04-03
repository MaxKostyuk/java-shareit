package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDTO;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer>, BookingRepositoryCustom {

    @Query(value = "select new ru.practicum.shareit.booking.dto.BookingDTO(bk.id, bk.start, bk.end, bk.status, bk.bookerId, it.id, it.name) " +
            "from Booking as bk " +
            "join Item as it on bk.itemId = it.id " +
            "where bk.bookerId = ?1 " +
            "order by bk.start desc")
    List<BookingDTO> getAllByBookerId(int bookerId);
}
