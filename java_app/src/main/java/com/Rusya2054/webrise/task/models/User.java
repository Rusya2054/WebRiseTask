package com.Rusya2054.webrise.task.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    public User() {};
    public User(String username){
        this.username = username;
    }
    public User(Long userId, String username){
        this.username = username;
        this.id = userId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "user_subscription", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "subscription_id"))
    private Set<Subscription> subscriptions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    @Override
    public int hashCode(){
        return username.hashCode()*31;
    }

    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass()) return false;
        if (o==this){ return true;}
        return ((User) o).getUsername().equals(this.getUsername());
    }
}
