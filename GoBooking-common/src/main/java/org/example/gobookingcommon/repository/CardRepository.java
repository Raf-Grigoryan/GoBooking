package org.example.gobookingcommon.repository;

import org.example.gobookingcommon.entity.user.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Integer> {

    boolean existsCardByCardNumber(String cardNumber);

    List<org.example.gobookingcommon.entity.user.Card> findCardByUserId(int userId);

    int id(int id);

    void deleteCardByCardNumber(String cardNumber);

    int countByUserId(int userId);

    Card findCardByCardNumber(String cardNumber);

    Optional<Card> findByUserId(int userId);

    Card findCardByUserIdAndPrimary(int userId, boolean primary );

}
