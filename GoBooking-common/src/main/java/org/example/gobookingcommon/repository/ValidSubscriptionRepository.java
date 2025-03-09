package org.example.gobookingcommon.repository;



import org.example.gobookingcommon.entity.company.Company;
import org.example.gobookingcommon.entity.subscription.ValidSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ValidSubscriptionRepository extends JpaRepository<ValidSubscription, Integer> {

    List<ValidSubscription> findByEndedDateBefore(Date endedDateBefore);

    List<ValidSubscription> findByEndedDate(Date endedDate);

    ValidSubscription findByCompany(Company company);
}
