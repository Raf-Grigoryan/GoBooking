package org.example.gobooking.repository;

import org.example.gobooking.entity.user.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card,Integer> {
    Optional<Card> findByCardNumber(String cardNumber);
}
