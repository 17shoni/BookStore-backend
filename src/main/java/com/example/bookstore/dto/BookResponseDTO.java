package com.example.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class BookResponseDTO {

    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private LocalDate publishedDate;
}
