package net.satyamthakur.librarybooksmanagementapi.repository;

import net.satyamthakur.librarybooksmanagementapi.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}