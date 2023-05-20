package com.task.lottery.services;

import com.task.lottery.entities.Ballot;
import com.task.lottery.entities.Lottery;
import com.task.lottery.entities.Participant;
import com.task.lottery.repositories.BallotRepository;
import com.task.lottery.repositories.LotteryRepository;
import com.task.lottery.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@EnableScheduling
public class LotteryService {

    @Autowired
    transient LotteryRepository lotteryRepository;

    @Autowired
    transient BallotRepository ballotRepository;

    @Autowired
    transient ParticipantRepository participantRepository;

    /**
     * Saves a new Lottery into the database.
     * If the startTime field is empty, set it to the current date.
     * @param lottery the lottery to save into the database.
     * @return the lottery
     * @throws Exception if the lottery already exists in the given day or the entered start date is a past day,
     * the lottery cannot be saved into the database.
     */
    public Lottery save(Lottery lottery) throws Exception {
        lottery.setIsOpen(true);
        if (lottery.getStartTime() == null){
            lottery.setStartTime(LocalDate.now());
        }
        else if (lottery.getStartTime().isAfter(LocalDate.now())){
            lottery.setIsOpen(false);
        }
        else if (lottery.getStartTime().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Cannot create lottery for a past date!");
        }
        if (lotteryExists(lottery.getStartTime())){
            throw new IllegalArgumentException("Lottery already exists!");
        }
        return lotteryRepository.save(lottery);
    }


    /**
     * Saves a lottery into the database.
     * This method is used to update the fields of the lottery entries that already exist in the database.
     * @param lottery the lottery to update the fields and save back into the database.
     * @return the lottery.
     */
    public Lottery updateFields (Lottery lottery){
        return lotteryRepository.save(lottery);
    }


    /**
     * Finds all the existing lotteries.
     * @return a list of all lotteries.
     */
    public List<Lottery> findAll() {
        return lotteryRepository.findAll();
    }

    public Optional<Lottery> findById(Long id) {
        return lotteryRepository.findById(id);
    }

    public void deleteById(Long id) {
        lotteryRepository.deleteAll(lotteryRepository.findAllById(List.of(id)));
    }


    /**
     * Closes the open lottery and picks a random winner.
     * Scheduled to run each day at midnight.
     */
    @Scheduled(cron = "0 59 23 * * ?", zone="CET")
    public void pickWinner() {
        List<Lottery> lotteryList = lotteryRepository.findByStartTime(LocalDate.now());
        for (Lottery lottery : lotteryList){
            List<Ballot> ballots = ballotRepository.findByLotteryId(lottery.getId());
            Random rand = new Random();
            Ballot winner = ballots.get(rand.nextInt(ballots.size()));
            lottery.setIsOpen(false);
            lottery.setWinnerId(winner.getId());
            updateFields(lottery);
            Participant winnerParticipant = participantRepository.findById(winner.getParticipantId()).get();
            winnerParticipant.setBalance(winnerParticipant.getBalance() + lottery.getPrize());
            participantRepository.save(winnerParticipant);
        }
    }


    /**
     * Starts the scheduled lotteries if the current date matches their start date.
     * Scheduled to run at the midnight of each day.
     */
    @Scheduled(cron = "0 0 0 * * ?", zone="CET")
    public void startLotteries () {
        List<Lottery> lotteryList = lotteryRepository.findByStartTime(LocalDate.now());
        if (!lotteryList.isEmpty()){
            for (Lottery lottery : lotteryList) {
                lottery.setIsOpen(true);
                updateFields(lottery);
            }
        }
    }


    /**
     * Gets the ballot that won the lottery of a specific date.
     * @param date the date
     * @return the winner ballot of the date
     * @throws Exception if the date entered is in the future, winner can't be returned.
     */
    public Ballot getWinner(LocalDate date) throws Exception{
        Ballot winner = null;
        if (date.isAfter(LocalDate.now()) || date.isEqual(LocalDate.now())){
            throw new IllegalArgumentException("Lottery not played yet!");
        }
        List<Lottery> lotteryList = lotteryRepository.findByStartTime(date);
        for (Lottery lottery : lotteryList) {
            winner = ballotRepository.findById(lottery.getWinnerId()).get();
        }
        return winner;
    }


    /**
     * Gets all the ballots that belongs to a lottery.
     * @param lotteryId the lottery id
     * @return a list of ballots that belongs to the lottery.
     */
    public List<Ballot> getAllBallotsForLottery (Long lotteryId){
        return ballotRepository.findByLotteryId(lotteryId);
    }


    /**
     * Checks whether a lottery already exists in the given date.
     * @param date the date.
     * @return true if there is a lottery in the given date and false otherwise.
     */
    public boolean lotteryExists(LocalDate date){
        return !lotteryRepository.findByStartTime(date).isEmpty();
    }

}
