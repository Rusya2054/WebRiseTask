package com.Rusya2054.webrise.task.repositories;

import com.Rusya2054.webrise.task.configurations.PrimaryDataBaseConfig;
import com.Rusya2054.webrise.task.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER, readOnly = true)
    @Query(value = """
        SELECT * FROM subscriptions_tb sbt
        WHERE sbt.user_id =:userId
        """, nativeQuery = true)
    Set<Subscription> findSubscriptionsByUserId(@Param("userId") Long userId);
}
