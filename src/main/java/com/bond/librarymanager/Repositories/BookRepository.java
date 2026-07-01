package com.bond.librarymanager.Repositories;

import com.bond.librarymanager.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {




    ArrayList<Book> findAllByTitleContainingIgnoreCase(String name);
    ArrayList<Book> findAllByAuthorContainingIgnoreCase(String author);
    ArrayList<Book> findAllByGenreContainingIgnoreCase(String genre);
    ArrayList<Book> findAllByIsbnContainingIgnoreCase(String isbn);
    ArrayList<Book> findAllByAvailableContaining(boolean isAvailable);

    boolean existsByTitleAndAuthorAndIsbn(String title, String author, String isbn);
    boolean existsByAuthorAndIsbn(String author, String isbn);
    boolean existsByTitle(String title);

    void deleteById(String id);


}
