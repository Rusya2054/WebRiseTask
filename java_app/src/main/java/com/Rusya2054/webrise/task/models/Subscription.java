package com.Rusya2054.webrise.task.models;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriptions_tb")
public class Subscription {
    public Subscription() {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "ru_name")
    private String ruName;
}
