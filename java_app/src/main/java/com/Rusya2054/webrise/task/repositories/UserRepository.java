package com.Rusya2054.webrise.task.repositories;

import com.Rusya2054.webrise.task.configurations.PrimaryDataBaseConfig;
import com.Rusya2054.webrise.task.models.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional(
            transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER, readOnly = true)
    @Query(value = """
            SELECT u FROM User u WHERE u.id = :id
            """)
    Optional<User> getUserById(@Param("id") Long userId);

    @Transactional(
            transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER, readOnly = true)
    @Query(value = """
            SELECT u FROM User u WHERE u.username = :username
            """)
    Optional<User> getUserByUsername(@Param("username") String username);


}
