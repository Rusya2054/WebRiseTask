package com.Rusya2054.webrise.task.repositories;

import com.Rusya2054.webrise.task.configurations.PrimaryDataBaseConfig;
import com.Rusya2054.webrise.task.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER, readOnly = true)
    @Query(value = """
        SELECT * FROM (select * from user_subscription us where us.user_id=:userId) tb
        JOIN subscriptions s ON s.id = tb.subscription_id
        """, nativeQuery = true)
    Set<Subscription> findSubscriptionsByUserId(@Param("userId") Long userId);

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER, readOnly = true)
    @Query(value = """
        SELECT * FROM subscriptions sbt where sbt.name=:name;
        """, nativeQuery = true)
    Optional<Subscription> findByName(@Param("name") String name);

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER, readOnly = true)
    @Query(value = """
        SELECT DISTINCT name FROM subscriptions sbt
        """, nativeQuery = true)
    Set<String> findAllSubscriptions();

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER, readOnly = true)
    @Query(value = """
            select s.* from (select us.subscription_id, count(us.subscription_id) as "top" from user_subscription us group by us.subscription_id limit :lim ) tb
            JOIN subscriptions s ON s.id = tb.subscription_id ORDER BY tb.top DESC
        """, nativeQuery = true)
    Set<Subscription> findTopSubscriptions(@Param("lim") Long limit);


}
