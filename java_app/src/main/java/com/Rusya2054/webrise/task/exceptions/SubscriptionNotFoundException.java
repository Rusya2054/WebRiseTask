
package com.Rusya2054.webrise.task.exceptions;

import java.io.IOException;

public class SubscriptionNotFoundException extends IOException {
    private Long subscriptionId;
    private String subscriptionName;

    private SubscriptionNotFoundException() {};
    public SubscriptionNotFoundException(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public SubscriptionNotFoundException(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    @Override
    public String getMessage() {
        return (subscriptionId!=null)?String.format("Subscription with id: '%s' is not found", this.subscriptionId):String.format("Subscription with name: '%s' is not found", this.subscriptionName);
    }
}
