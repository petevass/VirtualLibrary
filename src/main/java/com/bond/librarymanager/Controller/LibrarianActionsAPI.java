package com.bond.librarymanager.Controller;

import com.bond.librarymanager.Model.Book;
import com.bond.librarymanager.Model.Event;
import com.bond.librarymanager.Model.EventCreationRequest;
import com.bond.librarymanager.Repositories.BookRepository;
import com.bond.librarymanager.Repositories.EventRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/librarian/api")
public class LibrarianActionsAPI {

    private final BookRepository bookRepository;
    private final EventRepository eventRepository;

    public LibrarianActionsAPI(
            BookRepository bookRepository,
            EventRepository eventRepository) {

        this.bookRepository = bookRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/create_book")
    public ResponseEntity<String> createBook(@RequestBody Book book) {
        book.setAvailable(true);
        bookRepository.save(book);
        return ResponseEntity.ok("Book created");
    }

    @DeleteMapping("/remove_book")
    public ResponseEntity<String> removeBook(@RequestBody String bookId) {
        if (!bookRepository.existsById(bookId)) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }

        bookRepository.deleteById(bookId);
        return ResponseEntity.ok("Book removed");
    }

    @GetMapping("/get_events")
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @PostMapping("/add_event")
    public ResponseEntity<String> addEvent(
            @RequestBody EventCreationRequest eventRequest) {

        if (eventRequest.getName() == null ||
                eventRequest.getName().isBlank() ||
                eventRequest.getDescription() == null ||
                eventRequest.getDescription().isBlank() ||
                eventRequest.getDate() == null) {

            return ResponseEntity
                    .badRequest()
                    .body("Name, description, and date are required");
        }

        Event event = new Event();
        event.setName(eventRequest.getName().trim());
        event.setDescription(eventRequest.getDescription().trim());
        event.setDate(eventRequest.getDate());
        event.setUsersAttending(new ArrayList<>());

        eventRepository.save(event);

        return ResponseEntity.ok("Event successfully created");
    }

    @DeleteMapping("/delete_event")
    public ResponseEntity<String> deleteEvent(@RequestBody Event event) {
        if (event.getId() == null || !eventRepository.existsById(event.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
        }

        eventRepository.deleteById(event.getId());
        return ResponseEntity.ok("Event successfully deleted");
    }
}