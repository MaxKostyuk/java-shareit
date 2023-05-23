package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@Entity
@AllArgsConstructor
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Future
    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Future
    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
    @Positive
    @Column(name = "item", nullable = false)
    private Integer itemId;
    @Column(name = "booker_id", nullable = false)
    private Integer bookerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private BookingStatus status;
}
