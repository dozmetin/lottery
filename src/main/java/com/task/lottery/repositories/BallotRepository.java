package com.task.lottery.repositories;

import com.task.lottery.entities.Ballot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository used to access the Ballot table in the lottery database.
 */
@Repository("BallotRepository")
public interface BallotRepository extends JpaRepository<Ballot, Long> {
    List<Ballot> findByLotteryId(Long lotteryId);
}
