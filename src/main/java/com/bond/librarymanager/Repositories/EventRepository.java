package com.bond.librarymanager.Repositories;

import com.bond.librarymanager.Model.Book;
import com.bond.librarymanager.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {
    ArrayList<Event> findByDate(LocalDate Date);
    ArrayList<Event> findByName(String Name);

}
