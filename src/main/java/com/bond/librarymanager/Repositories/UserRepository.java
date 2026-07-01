package com.bond.librarymanager.Repositories;

import com.bond.librarymanager.Model.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, String> {

    ApplicationUser findByName(String username);
    ApplicationUser findByEmail(String email);
    ArrayList<ApplicationUser> findAllByRole(String role);
    boolean existsByEmail(String email);


}
