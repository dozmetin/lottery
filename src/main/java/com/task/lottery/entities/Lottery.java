package com.task.lottery.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Lottery {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            name = "lottery_id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "start_time"
    )
    private LocalDate startTime;

    @Column(
            name = "is_open"
    )
    private boolean isOpen;

    @Column(
            name = "ballot_count"
    )
    private int ballotCount;

    @Column(
            name = "winner_ballot_id"
    )
    private Long winnerBallotId;


    @Column(
            name = "prize"
    )
    private double prize;


    @Column(
            name = "ballot_price"
    )
    private double ballotPrice;

    public double getBallotPrice() {
        return ballotPrice;
    }

    public void setBallotPrice(double ballotPrice) {
        this.ballotPrice = ballotPrice;
    }

    /**
     * Instantiates a new Lottery
     * @param id - Id of the Lottery - Unique to each.
     * @param isOpen - Indicates whether the Lottery is currently open or not.
     * @param startTime - The start date of the Lottery.
     * @param ballotCount - Current number of ballots played in the Lottery.
     * @param winnerBallotId - The id of the ballot that won the Lottery.
     * @param prize - The prize that the Lottery winner will earn.
     * @param ballotPrice - The cost to play a ballot in the Lottery.
     */
    public Lottery(Long id, boolean isOpen, LocalDate startTime, int ballotCount, Long winnerBallotId,
                   double prize, double ballotPrice) {
        this.id = id;
        this.startTime = startTime;
        this.isOpen = isOpen;
        this.ballotCount = ballotCount;
        this.winnerBallotId = winnerBallotId;
        this.prize = prize;
        this.ballotPrice = ballotPrice;
    }


    public Lottery() {

    }


    /**
     * Gets the Lottery id.
     * @return the Lottery id.
     */
    public Long getId() {
        return id;
    }


    /**
     * Gets the start time of the Lottery.
     * @return the start time of the Lottery.
     */
    public LocalDate getStartTime() {
        return startTime;
    }


    /**
     * Sets the lottery start time.
     * @param startTime - the new start time to set.
     */
    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }


    /**
     * Gets the open/closed information of the Lottery.
     * @return true if the Lottery is open and false otherwise.
     */
    public boolean getIsOpen() {
        return isOpen;
    }


    /**
     * Changes the open/closed state of the Lottery.
     * @param isOpen new state to set.
     */
    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }


    /**
     * Gets the prize of the Lottery.
     * @return the prize of the Lottery.
     */
    public double getPrize() {
        return prize;
    }


    /**
     * Sets the prize of the Lottery.
     * @param prize the prize of the Lottery.
     */
    public void setPrize(double prize) {
        this.prize = prize;
    }


    /**
     * Gets the current played ballot count in the Lottery.
     * @return the ballot count in the Lottery.
     */
    public int getBallotCount() {
        return ballotCount;
    }


    /**
     * Sets the ballot count of the Lottery.
     * @param ballotCount - the new ballot count to set.
     */
    public void setBallotCount(int ballotCount) {
        this.ballotCount = ballotCount;
    }


    /**
     * Gets the id the of the Ballot that won the Lottery.
     * @return - the id of the winner Ballot.
     */
    public Long getWinnerId() {
        return winnerBallotId;
    }


    /**
     * Sets the id of the winner Ballot in the Lottery.
     * @param winnerId - the new Ballot id to set.
     */
    public void setWinnerId(Long winnerId) {
        this.winnerBallotId = winnerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lottery lottery = (Lottery) o;
        return isOpen == lottery.isOpen && ballotCount == lottery.ballotCount && Double.compare(lottery.prize, prize) == 0 && Double.compare(lottery.ballotPrice, ballotPrice) == 0 && Objects.equals(id, lottery.id) && Objects.equals(startTime, lottery.startTime) && Objects.equals(winnerBallotId, lottery.winnerBallotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, isOpen, ballotCount, winnerBallotId, prize, ballotPrice);
    }
}
