package org.example.gobooking.repository;

import org.example.gobooking.entity.user.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card,Integer> {

  boolean  existsCardByCardNumber(String cardNumber);

  int countByUserId(int userId);
}
