package org.example.gobooking.repository;


import org.example.gobooking.entity.subscription.ValidSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValidSubscriptionRepository extends JpaRepository<ValidSubscription, Integer> {

}
