package org.example.entity;

import jakarta.persistence.Entity;

@Entity
public class Customer extends User {

    private Integer loyalPoints;

    public Integer getLoyalPoints() {
        return loyalPoints;
    }

    public void setLoyalPoints(Integer loyalPoints) {
        this.loyalPoints = loyalPoints;
    }
}
