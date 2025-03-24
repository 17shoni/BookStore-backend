package com.example.bookstore.services;

import com.example.bookstore.dto.BookRequestDTO;
import com.example.bookstore.dto.BookResponseDTO;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<BookResponseDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    public BookResponseDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
    }

    public BookResponseDTO addBook(BookRequestDTO bookRequestDTO) {
        Book book = mapToEntity(bookRequestDTO);
        return mapToDTO(bookRepository.save(book));
    }

    public BookResponseDTO updateBook(Long id, BookRequestDTO bookRequestDTO) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookRequestDTO.getTitle());
                    book.setAuthor(bookRequestDTO.getAuthor());
                    book.setPrice(bookRequestDTO.getPrice());
                    book.setPublishedDate(bookRequestDTO.getPublishedDate());
                    return mapToDTO(bookRepository.save(book));
                })
                .orElseThrow(() -> new BookNotFoundException("Book with ID " + id + " not found"));
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    private BookResponseDTO mapToDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setPrice(book.getPrice());
        dto.setPublishedDate(book.getPublishedDate());
        return dto;
    }

    private Book mapToEntity(BookRequestDTO dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .price(dto.getPrice())
                .publishedDate(dto.getPublishedDate())
                .build();
    }
}



