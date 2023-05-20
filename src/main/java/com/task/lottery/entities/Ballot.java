package com.task.lottery.entities;

import jakarta.persistence.*;


import java.util.Objects;

@Entity
public class Ballot {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "ballot_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "lottery_id",
            updatable = false,
            nullable = false
    )
    private Long lotteryId;

    @Column(
            name = "participant_id",
            updatable = false,
            nullable = false
    )

    private Long participantId;


    @Column(
            name = "guess"
    )
    private String guess;

    /**
     * Instantiates a new Ballot
     * @param id - Id of the Ballot - Unique to each.
     * @param lotteryId - The id of the Lottery in which the Ballot was played in.
     * @param participantId - The id of the Participant who played the Ballot.
     * @param guess - The ballot guess.
     */
    public Ballot(Long id, Long lotteryId, Long participantId, String guess) {
        this.id = id;
        this.lotteryId = lotteryId;
        this.participantId = participantId;
        this.guess = guess;
    }

    public Ballot() {

    }

    /**
     * Gets the Ballot id.
     * @return the Ballot id
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the Lottery id in which the Ballot was played in.
     * @return the Lottery id.
     */
    public Long getLotteryId() {
        return lotteryId;
    }

    /**
     * Gets the id of the Participant who played the Ballot.
     * @return the Participant id.
     */
    public Long getParticipantId() {
        return participantId;
    }

    /**
     * Gets the guess number sequence of the Ballot.
     * @return the guess.
     */
    public String getGuess() {
        return guess;
    }

    /**
     * Sets the quess number sequence of the Ballot.
     * @param guess - New guess to set.
     */
    public void setGuess(String guess) {
        this.guess = guess;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ballot ballot = (Ballot) o;
        return Objects.equals(id, ballot.id) && Objects.equals(lotteryId, ballot.lotteryId) && Objects.equals(participantId, ballot.participantId) && Objects.equals(guess, ballot.guess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lotteryId, participantId, guess);
    }
}
