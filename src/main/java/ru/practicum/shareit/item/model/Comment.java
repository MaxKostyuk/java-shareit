package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @NotBlank
    private String text;
    @Column(nullable = false)
    private int item;
    @Column(nullable = false)
    private int author;
    @Column(nullable = false)
    private LocalDateTime created;
}
