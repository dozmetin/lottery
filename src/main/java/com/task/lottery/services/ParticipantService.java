package com.task.lottery.services;

import com.task.lottery.entities.Participant;
import com.task.lottery.repositories.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;


    /**
     * Instantiates a new Participant Service.
     * @param participantRepository the participant repository.
     */
    @Autowired
    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }


    /**
     * Saves a participant into the database.
     * @param participant the participant to save.
     * @return the participant.
     */
    public Participant save(Participant participant) {
        return participantRepository.save(participant);
    }


    /**
     * Finds all the participants.
     * @return a list of all the participants.
     */
    public List<Participant> findAll() {
        return participantRepository.findAll();
    }


    /**
     * Finds a participant by its id.
     * @param id the id of the participant.
     * @return an optional of the participant.
     */
    public Optional<Participant> findById(long id) {
        return participantRepository.findById(id);
    }


    /**
     * Finds a participant by its id and removes it from the database.
     * @param id the id of the participant.
     */
    public void deleteById(long id) {
        participantRepository.deleteAll(participantRepository.findAllById(List.of(id)));
    }

}
