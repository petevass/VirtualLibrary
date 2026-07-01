package com.bond.librarymanager.Repositories;

import com.bond.librarymanager.Model.Book;
import com.bond.librarymanager.Model.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

public interface CheckoutRepository extends JpaRepository<Checkout, String> {

    ArrayList<Checkout> findByUserId(String id);
    Checkout findByBookId(String bookId);
    Checkout findByUserIdAndBookId(String userId, String bookId);
    @Transactional
    Checkout removeCheckoutByUserIdAndBookId(String userId, String bookId);

}
