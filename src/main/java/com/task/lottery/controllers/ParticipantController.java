package com.task.lottery.controllers;

import com.task.lottery.entities.Participant;
import com.task.lottery.services.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping(path="/participants")
public class ParticipantController {

    @Autowired
    transient ParticipantService service;


    /**
     * Receives a POST mapping to save a participant instance into the database.
     * @param participant the lottery to save.
     * @return ResponseEntity with ok message and participant as its body if the participant was successfully saved.
     * Otherwise, return ResponseEntity with badRequest message and the error message as its body.
     */
    @PostMapping(path = "/participant")
    public @ResponseBody
    ResponseEntity<Participant> addNewParticipant(
            @RequestBody Participant participant) {
        return ResponseEntity.ok(service.save(participant));
    }


    /**
     * Receives a GET mapping to return all the participants in the database.
     * @return ResponseEntity containing a list of all the participants in the database.
     */
    @GetMapping(path = "/list")
    public @ResponseBody ResponseEntity<List<Participant>> getAllContracts() {
        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Recevies a DELETE mapping to remove a participant from the database.
     * @param participantId the id the participant.
     * @return ResponseEntity with ok if the participant was found. Otherwise, ResponseEntity with unprocessableEntity.
     */
    @DeleteMapping
    public @ResponseBody ResponseEntity<?> deleteParticipant(@RequestBody Long participantId) {
        try {
            service.deleteById(participantId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity()
                    .body("The entity could not be deleted because the entity was not found.");
        }
    }


    /**
     * Receives a GET mapping to find a participant from the database by its id and return it.
     * @param id the id of the participant.
     * @return ResponseEntity with the participant if present in the database.
     * Otherwise, ResponseEntity with unprocessableEntity and the error message in the body.
     */
    @GetMapping(path = "/participant/{id}")
    public ResponseEntity<?> getParticipant(
            @PathVariable Long id) {
        Optional<Participant> participant = service.findById(id);
        if (participant.isPresent()) {
            return ResponseEntity.ok(participant.get());
        } else {
            return ResponseEntity.unprocessableEntity()
                    .body("The element was not found in the db.");
        }
    }

}
