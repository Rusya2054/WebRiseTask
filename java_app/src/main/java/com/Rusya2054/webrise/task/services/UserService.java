package com.Rusya2054.webrise.task.services;

import com.Rusya2054.webrise.task.configurations.PrimaryDataBaseConfig;

import com.Rusya2054.webrise.task.exceptions.SubscriptionNotFoundException;
import com.Rusya2054.webrise.task.exceptions.UserExistsException;
import com.Rusya2054.webrise.task.exceptions.UserNotFoundException;
import com.Rusya2054.webrise.task.models.Subscription;
import com.Rusya2054.webrise.task.models.User;
import com.Rusya2054.webrise.task.models.db.DataBaseProperty;
import com.Rusya2054.webrise.task.repositories.SubscriptionRepository;
import com.Rusya2054.webrise.task.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public UserService(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    public boolean isExists(String username){
        Optional<User> optionalUser = userRepository.getUserByUsername(username);
        return optionalUser.isPresent();
    }

    public User getUserById(Long userId) throws UserNotFoundException {
        return userRepository.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
    }
    public void createUser(String username) {
        User savedUser = userRepository.save(new User(username));
        log.info("Created new user with usename: [{}], id: [{}]", savedUser.getUsername(), savedUser.getId());
    }

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = {UserNotFoundException.class}
    )
    public User updateUser(Long userId, String username) throws UserNotFoundException{
        User user = userRepository.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        User newUserRef = new User(userId, username.trim());
        userRepository.save(newUserRef);
        log.info("Updated user with usename: [{}], id: [{}]", newUserRef.getUsername(), newUserRef.getId());
        return newUserRef;
    }

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = {UserNotFoundException.class}
    )
    public Subscription updateUsersSubscription(Long userId, String subscriptionName) throws UserNotFoundException, SubscriptionNotFoundException {
        User user = userRepository.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Subscription subscription = subscriptionRepository.findByName(subscriptionName).orElseThrow(()-> new SubscriptionNotFoundException(subscriptionName));
        user.getSubscriptions().add(subscription);
        userRepository.save(user);
        userRepository.flush();
        log.info("Updated user with usename: [{}], id: [{}]. Added subscription: {}", user.getUsername(), user.getId(), subscription.getName());
        return subscription;
    }

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = {UserNotFoundException.class}
    )
    public void deleteUserById(Long userId) throws UserNotFoundException{
        User user = userRepository.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Set<Subscription> userSubscriptions = subscriptionRepository.findSubscriptionsByUserId(user.getId());
        userRepository.delete(user);
        log.info("Deleted user with id: [{}] with {} subscriptions", user.getUsername(), userSubscriptions.size());
    }

    @Transactional(transactionManager = PrimaryDataBaseConfig.TRANSACTION_MANAGER,
            isolation = Isolation.READ_COMMITTED,
            rollbackFor = {UserNotFoundException.class}
    )
    public void deleteSubscriptionById(Long userId, Long subscriptionId) throws UserNotFoundException, SubscriptionNotFoundException {
        User user = userRepository.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(()-> new SubscriptionNotFoundException(subscriptionId));
        user.getSubscriptions().remove(subscription);
        userRepository.save(user);
        log.info("Deleted subscription with id: {} of user with id: [{}]", subscriptionId, userId);
    }

    public Set<Subscription> getUsersSubscriptions(Long userId) throws UserNotFoundException {
        User user = userRepository.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        return subscriptionRepository.findSubscriptionsByUserId(user.getId());
    }
    public Set<Subscription> getTopSubscriptions(Long limit) {
        return subscriptionRepository.findTopSubscriptions(limit);
    }
}
