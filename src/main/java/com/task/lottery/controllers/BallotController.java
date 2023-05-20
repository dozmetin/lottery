package com.task.lottery.controllers;


import com.task.lottery.entities.Ballot;
import com.task.lottery.services.BallotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping(path="/ballots")
public class BallotController {
    @Autowired
    transient BallotService service;

    /**
     * Receives a POST mapping to save a ballot instance into the database.
     * @param ballot the ballot to save.
     * @return ResponseEntity with ok message and ballot as its body if the ballot was successfully saved.
     * Otherwise, return ResponseEntity with badRequest message and the error message as its body.
     */
    @PostMapping(path = "/ballot")
    public @ResponseBody
    ResponseEntity<?> createBallot(
            @RequestBody Ballot ballot) {
        try {return ResponseEntity.ok(service.save(ballot)); }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Ballot can't be created!");
        }
    }


    /**
     * Receives a GET mapping to return all the ballots in the database.
     * @return ResponseEntity containing a list of all the ballots in the database.
     */
    @GetMapping(path = "/list")
    public @ResponseBody ResponseEntity<List<Ballot>> getAllBallots() {
        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Recevies a DELETE mapping to remove a ballot from the database.
     * @param ballotId the id the ballot.
     * @return ResponseEntity with ok if the ballot was found. Otherwise ResponseEntity with unprocessableEntity.
     */
    @DeleteMapping
    public @ResponseBody ResponseEntity<?> deleteBallot(@RequestBody Long ballotId) {
        try {
            service.deleteById(ballotId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity()
                    .body("The entity could not be deleted because the entity was not found.");
        }
    }


    /**
     * Receives a GET mapping to find a ballot from the database by its id and return it.
     * @param id the id of the ballot.
     * @return ResponseEntity with the ballot if present in the database.
     * Otherwise, ResponseEntity with unprocessableEntity and the error message in the body.
     */
    @GetMapping(path = "/ballot/{id}")
    public ResponseEntity<?> getBallot(
            @PathVariable Long id) {
        Optional<Ballot> lottery = service.findById(id);
        if (lottery.isPresent()) {
            return ResponseEntity.ok(lottery.get());
        } else {
            return ResponseEntity.unprocessableEntity()
                    .body("The element was not found in the db.");
        }
    }
}
