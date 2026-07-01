package com.bond.librarymanager.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    String id;

    @Column(nullable = false)
    String bookId;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    LocalDate checkoutdate;

    @Column(nullable = false)
    LocalDate duedate;



}
