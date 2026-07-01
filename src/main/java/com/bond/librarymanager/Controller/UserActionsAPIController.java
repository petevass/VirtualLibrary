package com.bond.librarymanager.Controller;

import com.bond.librarymanager.Model.Book;
import com.bond.librarymanager.Model.Checkout;
import com.bond.librarymanager.Model.ApplicationUser;
import com.bond.librarymanager.Model.Event;
import com.bond.librarymanager.Repositories.BookRepository;
import com.bond.librarymanager.Repositories.CheckoutRepository;
import com.bond.librarymanager.Repositories.EventRepository;
import com.bond.librarymanager.Repositories.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/user/")
public class UserActionsAPIController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CheckoutRepository checkoutRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;

    public UserActionsAPIController(BookRepository bookRepository, CheckoutRepository checkoutRepository, UserRepository userRepository,  EventRepository eventRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/get_books")
    public ResponseEntity<ArrayList<Book>> getBooksBy(@RequestParam String category, @RequestParam String req ){
        ArrayList<Book> finalList =  new ArrayList<>();
        if(category.equals("author")){
            finalList = bookRepository.findAllByAuthorContainingIgnoreCase(req);

        }else if(category.equals("title")){
            finalList = bookRepository.findAllByTitleContainingIgnoreCase(req);
        }else if(category.equals("genre")){
            finalList = bookRepository.findAllByGenreContainingIgnoreCase(req);
        }else if(category.equals("isbn")){
            finalList = bookRepository.findAllByIsbnContainingIgnoreCase(req);
        }else if(category.equals("available")){
            finalList = bookRepository.findAllByAvailableContaining(Boolean.parseBoolean(req));
        }
        return  ResponseEntity.ok(finalList);
    }

    @GetMapping("/get_all_books")
    public ResponseEntity<List<Book>> getAllBooks(){

        return ResponseEntity.ok(bookRepository.findAll());
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> checkOutBook(@RequestBody Book book, @AuthenticationPrincipal OAuth2User principle){

        if(!book.isAvailable()){

            return ResponseEntity.status(400).body("Book Already Checked Out");

        }


        if (!userRepository.existsByEmail(principle.getAttribute("email"))) {
            return ResponseEntity.status(400).body("User Not Found");
        }
        ApplicationUser applicationUser =  userRepository.findByEmail(principle.getAttribute("email"));
        book.setAvailable(false);
        bookRepository.save(book);
        LocalDate checkoutdate = LocalDate.now();
        LocalDate duedate = checkoutdate.plusWeeks(4);
        Checkout checkout = new Checkout();
        checkout.setBookId(book.getId());
        checkout.setUserId(applicationUser.getId());
        checkout.setCheckoutdate(checkoutdate);
        checkout.setDuedate(duedate);
        checkoutRepository.save(checkout);

        return ResponseEntity.status(200).body("Book Checked Out Successfully");
    }

    @PostMapping("/return_book")
    public ResponseEntity<String> returnBook(@RequestBody String bookId, @AuthenticationPrincipal OAuth2User principle){

        System.out.println(bookId.replace("\"", ""));
        bookId = bookId.replace("\"", "");
        if(!bookRepository.existsById(bookId)){
            return ResponseEntity.status(400).body("Book Not Found");
        }
        Book book = bookRepository.findById(bookId).get();
        if(book.isAvailable()){
            return ResponseEntity.status(400).body("Book Already Returned");
        }

        if (!userRepository.existsByEmail(principle.getAttribute("email"))) {
            return ResponseEntity.status(400).body("User Not Found");
        }
        ApplicationUser applicationUser =  userRepository.findByEmail(principle.getAttribute("email"));
        checkoutRepository.removeCheckoutByUserIdAndBookId(applicationUser.getId(), book.getId());

        book.setAvailable(true);
        bookRepository.save(book);

        return ResponseEntity.status(200).body("Book Returned Successfully");
    }


    @PostMapping("/event_sign_up")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> signUpForEvent(@RequestBody Event event,@RequestParam String userID ){

        if(!eventRepository.existsById(event.getId())){
            return ResponseEntity.status(400).body("Event Not Found");
        }

        List<String> users= event.getUsersAttending();
        users.add(userID);
        event.setUsersAttending(users);
        eventRepository.save(event);



        return ResponseEntity.status(200).body("Event Sign Up Successfully");
    }

    @GetMapping("/get_events")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Event>> getAllEvents(){

        return ResponseEntity.ok(eventRepository.findAll());
    }

    @GetMapping("/my_checkouts")
    public ResponseEntity getMyCheckouts(@AuthenticationPrincipal OAuth2User principle){
        if(!userRepository.existsByEmail(principle.getAttribute("email"))) {
            return ResponseEntity.badRequest().body("User Does not Exit");
        }
        ApplicationUser user =  userRepository.findByEmail(principle.getAttribute("email"));
        ArrayList<Book> books = new ArrayList<>();
        for(Checkout c: checkoutRepository.findByUserId(user.getId())){
            books.add(bookRepository.findById(c.getBookId()).get());
        }

        return ResponseEntity.status(200).body(books);
    }


}
