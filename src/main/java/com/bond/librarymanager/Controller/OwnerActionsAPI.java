package com.bond.librarymanager.Controller;


import com.bond.librarymanager.Model.ApplicationUser;
import com.bond.librarymanager.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("owner/api")
public class OwnerActionsAPI {

@Autowired
UserRepository userRepository;

public OwnerActionsAPI(UserRepository userRepository){
    this.userRepository = userRepository;
}

    @PostMapping("/make_user_admin")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> makeUserAdmin(@RequestBody ApplicationUser applicationUser){

    applicationUser.setRole("ADMIN");
    userRepository.save(applicationUser);
    return ResponseEntity.ok("Successfully Updated User Roles");

    }

    @PostMapping("/make_user_owner")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<String> makeUserOwner(@RequestBody ApplicationUser applicationUser){


    applicationUser.setRole("OWNER");
    userRepository.save(applicationUser);
    return ResponseEntity.ok("Successfully Updated User Roles");
    }

}
