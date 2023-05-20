package com.task.lottery.repositories;

import com.task.lottery.entities.Lottery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


/**
 * Repository used to access the Lottery table in the lottery database.
 */
@Repository("LotteryRepository")
public interface LotteryRepository extends JpaRepository<Lottery, Long> {
    List<Lottery> findByStartTime(LocalDate startTime);
}
