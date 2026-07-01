package com.bond.librarymanager.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    String description;

    @Column(nullable = false)
    String genre;

    @Column(nullable = false, name="is_paperback")
    boolean isPaperback;

    @Column(nullable = false, name="is_available")
    boolean available;

    @Column(nullable = false)
    String isbn;

}
