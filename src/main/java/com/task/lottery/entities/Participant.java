package com.task.lottery.entities;
import jakarta.persistence.*;

import java.util.Objects;


@Entity
public class Participant {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "first_name",
            updatable = false
    )
    private String firstName;

    @Column(
            name = "last_name",
            updatable = false
    )
    private String lastName;


    @Column(
            name = "email"
    )
    private String email;

    @Column(
            name = "balance"
    )
    private double balance;

    /**
     * Instantiates a new Participant
     * @param id Id of the Participant - Unique to each.
     * @param firstName First name of the Participant.
     * @param lastName Last name of the Participant.
     * @param balance Current balance of the Participant.
     */
    public Participant(long id, String firstName, String lastName, String email,double balance) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.email = email;
    }

    public Participant() {

    }


    /**
     * Gets the id of the Participant.
     * @return the id of the Participant.
     */
    public long getId() {
        return id;
    }


    /**
     * Gets the first name of the Participant.
     * @return the first name of the Participant.
     */
    public String getFirstName() {
        return firstName;
    }



    /**
     * Gets the last name of the Participant.
     * @return the last name of the Participant.
     */
    public String getLastName() {
        return lastName;
    }



    /**
     * Gets the current balance of the Participant.
     * @return the balance of the Participant.
     */
    public double getBalance() {
        return balance;
    }


    /**
     * Gets the email of the Participant.
     * @return the email of the Participant.
     */
    public String getEmail() {
        return email;
    }


    /**
     * Sets the email of the Participant
     * @param email the new email of the Participant to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets a new balance for the Participant.
     * @param balance the new balance to set.
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Double.compare(that.balance, balance) == 0 && Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, balance);
    }
}
