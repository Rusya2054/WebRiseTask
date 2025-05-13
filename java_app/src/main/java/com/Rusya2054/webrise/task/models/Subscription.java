package com.Rusya2054.webrise.task.models;

import jakarta.persistence.*;

@Entity
@Table(name = "subscriptions")
public class Subscription {
    public Subscription() {};
    public Subscription(String name) {
        this.name = name;
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    public String getName() {
        return name;
    }
    @Override
    public int hashCode(){
        return name.hashCode()*31;
    }

    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass()) return false;
        if (o==this){ return true;}
        return ((Subscription) o).getName().equals(this.getName());
    }
}
