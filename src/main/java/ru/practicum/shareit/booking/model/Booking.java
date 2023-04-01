package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    @Column(name = "start_date",nullable = false)
    private LocalDateTime start;
    @Column(name ="end_date", nullable = false)
    private LocalDateTime end;
    @Column(nullable = false)
    private Integer item;
    @Column(nullable = false)
    private Integer bookerId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private BookingStatus status;
}
