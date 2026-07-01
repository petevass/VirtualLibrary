package com.bond.librarymanager.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    String Id;

    @Column(nullable = false)
    String Name;

    @Column(nullable = false)
    String Description;

    @Column(nullable = false)
    LocalDate Date;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> usersAttending = new ArrayList<>();
}
