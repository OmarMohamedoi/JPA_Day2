package org.example.entity;

import jakarta.persistence.Entity;

@Entity
public class Employee extends User{

    private Double salary;
    private String department;

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
