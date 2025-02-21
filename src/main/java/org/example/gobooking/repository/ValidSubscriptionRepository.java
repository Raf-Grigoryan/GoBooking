package org.example.gobooking.repository;


import org.example.gobooking.entity.subscription.ValidSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ValidSubscriptionRepository extends JpaRepository<ValidSubscription, Integer> {

    List<ValidSubscription> findByEndedDateBefore(Date endedDateBefore);

    List<ValidSubscription> findByEndedDate(Date endedDate);
}
