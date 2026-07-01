package com.bond.librarymanager.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OverseerPagesController {

    @GetMapping("/librarian")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'OWNER')")
    public String librarianPage() {
        return "LibrarianPages/librarian";
    }

    @GetMapping("/librarian/add-book")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'OWNER')")
    public String addBook(){
        return "LibrarianPages/add-book";
    }

    @GetMapping("/librarian/add-event")
    @PreAuthorize("hasAnyRole('LIBRARIAN','OWNER')")
    public String addEvent() {
        return "LibrarianPages/add-event";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyRole('ADMIN','OWNER')")
    public String admin() {
        return "AdminPages/admin";
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER')")
    public String owner() {
        return "OwnerPages/owner";
    }


}
