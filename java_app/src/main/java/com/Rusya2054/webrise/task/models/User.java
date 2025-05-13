package com.Rusya2054.webrise.task.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "users_tb")
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

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = false )
    @JoinColumn(unique = false, name = "user_id", referencedColumnName = "id")
    private Set<Subscription> subscriptions = Set.of();

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }
}
