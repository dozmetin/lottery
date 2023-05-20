package com.task.lottery.services;

import com.task.lottery.entities.Ballot;
import com.task.lottery.entities.Lottery;
import com.task.lottery.entities.Participant;
import com.task.lottery.repositories.BallotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BallotService {

    private BallotRepository ballotRepository;
    private LotteryService lotteryService;
    private ParticipantService participantService;

    int guessLength = 6;

    /**
     * Instantiates a new BallotService.
     * @param ballotRepository the ballot repository
     * @param lotteryService the lottery service
     * @param participantService the participant service
     */
    @Autowired
    public BallotService(BallotRepository ballotRepository,
                         LotteryService lotteryService,
                         ParticipantService participantService) {
        this.ballotRepository = ballotRepository;
        this.lotteryService = lotteryService;
        this.participantService = participantService;
    }


    /**
     * Saves a new Ballot to the database.
     * A ballot entry can only be created if it is played by an existing participant with enough balance in an open lottery.
     *
     * @param ballot the Ballot to save.
     * @return the Ballot
     * @throws Exception ballot cannot be played if the lottery or participant does not exist and
     * if the participant does not have enough balance.
     */
    public Ballot save(Ballot ballot) throws Exception {
        if (lotteryService.findById(ballot.getLotteryId()).isPresent()
                && participantService.findById(ballot.getParticipantId()).isPresent()){
            Lottery lottery = lotteryService.findById(ballot.getLotteryId()).get();
            Participant participant = participantService.findById(ballot.getParticipantId()).get();
            if (lottery.getIsOpen() && participant.getBalance() >= lottery.getBallotPrice()
                    && ballot.getGuess().matches("[0-9]+") && ballot.getGuess().length() == guessLength){
                lottery.setBallotCount(lottery.getBallotCount() + 1);
                lotteryService.updateFields(lottery);
                participant.setBalance(participant.getBalance() - lottery.getBallotPrice());
                participantService.save(participant);
                return ballotRepository.save(ballot);
            }
        }
        throw new Exception("Ballot not present!");
    }


    /**
     * Finds all the ballots.
     * @return a list of all ballots.
     */
    public List<Ballot> findAll() {
        return ballotRepository.findAll();
    }

    /**
     * Finds a ballot by its id.
     * @param id the id of the ballot to find.
     * @return the ballot.
     */
    public Optional<Ballot> findById(long id) {
        return ballotRepository.findById(id);
    }


    /**
     * Finds a ballot by its id and removes it from the database.
     * @param id the id of the ballot to remove from the database.
     */
    public void deleteById(long id) {
        ballotRepository.deleteAll(ballotRepository.findAllById(List.of(id)));
    }
}
