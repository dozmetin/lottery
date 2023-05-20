package com.task.lottery.repositories;

import com.task.lottery.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository used to access the Participant table in the lottery database.
 */
@Repository("ParticipantRepository")
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findParticipantByEmail(String eMail);
}
