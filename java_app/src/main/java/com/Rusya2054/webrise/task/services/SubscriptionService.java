package com.Rusya2054.webrise.task.services;

import com.Rusya2054.webrise.task.models.Subscription;
import com.Rusya2054.webrise.task.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository repository) {
        this.subscriptionRepository = repository;
    }


    public Set<String> getAllSubscriptions(){
        return subscriptionRepository.findAllSubscriptions();
    }
}
