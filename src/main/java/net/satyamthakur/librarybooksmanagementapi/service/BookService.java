package net.satyamthakur.librarybooksmanagementapi.service;

import net.satyamthakur.librarybooksmanagementapi.model.Book;
import net.satyamthakur.librarybooksmanagementapi.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookByIsbn(Long isbn) {
        return bookRepository.findById(isbn);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public boolean existsByIsbn(Long isbn) {
        return bookRepository.existsById(isbn);
    }

    public void deleteBook(Long isbn) {
        bookRepository.deleteById(isbn);
    }
}