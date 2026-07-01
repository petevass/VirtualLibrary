package com.bond.librarymanager.Controller;

import com.bond.librarymanager.Model.ApplicationUser;
import com.bond.librarymanager.Repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
public class AdministratorActionsAPI {

    @Autowired
    UserRepository userRepository;

    public AdministratorActionsAPI(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/make_user_librarian")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> makeUserLibrarian(@AuthenticationPrincipal OAuth2User principle, @RequestBody ApplicationUser applicationUser) {

        if(!userRepository.existsByEmail(principle.getAttribute("email"))){
            return ResponseEntity.badRequest().body("You Are Not a User");
        }

        ApplicationUser user = userRepository.findByEmail(principle.getAttribute("email"));

    if(!user.getRole().equals("ADMIN")){
        if(!user.getRole().equals("OWNER")){
            return ResponseEntity.badRequest().body("You Are Not an Owner or Admin");
        }
    }

    applicationUser.setRole("LIBRARIAN");
    userRepository.save(applicationUser);
    return ResponseEntity.ok().body("Success");
    }

    @PostMapping("/make_librarian_user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> makeLibrarianUser(@AuthenticationPrincipal OAuth2User principle, @RequestBody ApplicationUser applicationUser) {

        if(!userRepository.existsByEmail(principle.getAttribute("email"))){
            return ResponseEntity.badRequest().body("You Are Not a User");
        }

        ApplicationUser user = userRepository.findByEmail(principle.getAttribute("email"));

        if(!user.getRole().equals("ADMIN")){
            if(!user.getRole().equals("OWNER")){
                return ResponseEntity.badRequest().body("You Are Not an Owner or Admin");
            }
        }

        applicationUser.setRole("USER");
        userRepository.save(applicationUser);
        return ResponseEntity.ok().body("Success");
    }

    @GetMapping("/get_users")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<List<ApplicationUser>> getUsers(@RequestParam(required = false) String role){

        if(role != null){
            return ResponseEntity.ok(userRepository.findAllByRole(role));
        }
        return ResponseEntity.ok(userRepository.findAll());

    }


}
