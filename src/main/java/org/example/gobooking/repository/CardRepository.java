package org.example.gobooking.repository;

import org.example.gobooking.entity.user.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {

    boolean existsCardByCardNumber(String cardNumber);

    List<Card> findCardByUserId(int userId);

    int id(int id);

    void deleteCardByCardNumber(String cardNumber);
}
