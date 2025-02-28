package org.example.gobookingcommon.repository;


import org.example.gobookingcommon.entity.subscription.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SubscriptionRepository extends JpaRepository<Subscription,Integer> {

    boolean existsSubscriptionByTitle(String title);

    Subscription findSubscriptionByTitle(String title);

}
