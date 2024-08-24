package com.simple_form.model;
import jakarta.persistence.*;

@Entity
@Table(name = "CustomersDetails")
public class CustomersDetailsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String emailId;
    private String periodCollectedFor;
    private String dueDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPeriodCollectedFor() {
        return periodCollectedFor;
    }

    public void setPeriodCollectedFor(String periodCollectedFor) {
        this.periodCollectedFor = periodCollectedFor;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
// Getters and setters
    // ...
}
