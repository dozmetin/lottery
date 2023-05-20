package com.task.lottery.controllers;


import com.task.lottery.entities.Lottery;
import com.task.lottery.services.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RestController
@RequestMapping(path="/lotteries")
public class LotteryController {

    @Autowired
    transient LotteryService service;


    /**
     * Receives a POST mapping to save a lottery instance into the database.
     * @param lottery the lottery to save.
     * @return ResponseEntity with ok message and lottery as its body if the ballot was successfully saved.
     * Otherwise, return ResponseEntity with badRequest message and the error message as its body.
     */
    @PostMapping(path = "/lottery")
    public @ResponseBody
    ResponseEntity<?> createLottery(
            @RequestBody Lottery lottery) {
        try { return ResponseEntity.ok(service.save(lottery));}
        catch (Exception e) {
            return ResponseEntity.badRequest().body("You can only create 1 lottery for a day!");
        }
    }


    /**
     * Receives a GET mapping to return all the lotteries in the database.
     * @return ResponseEntity containing a list of all the lotteries in the database.
     */
    @GetMapping(path = "/list")
    public @ResponseBody ResponseEntity<List<Lottery>> getAllLotteries() {
        return ResponseEntity.ok(service.findAll());
    }


    /**
     * Recevies a DELETE mapping to remove a ballot from the database.
     * @param lotteryId the id the ballot.
     * @return ResponseEntity with ok if the lottery was found. Otherwise, ResponseEntity with unprocessableEntity.
     */
    @DeleteMapping
    public @ResponseBody ResponseEntity<?> deleteLottery(@RequestBody Long lotteryId) {
        try {
            service.deleteById(lotteryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity()
                    .body("The entity could not be deleted because the entity was not found.");
        }
    }


    /**
     * Receives a GET mapping to find a lottery from the database by its id and return it.
     * @param id the id of the lottery.
     * @return ResponseEntity with the lottery if present in the database.
     * Otherwise, ResponseEntity with unprocessableEntity and the error message in the body.
     */
    @GetMapping(path = "/lottery/{id}")
    public ResponseEntity<?> getLottery(
            @PathVariable Long id) {
        Optional<Lottery> lottery = service.findById(id);
        if (lottery.isPresent()) {
            return ResponseEntity.ok(lottery.get());
        } else {
            return ResponseEntity.unprocessableEntity()
                    .body("The element was not found in the db.");
        }
    }

    /**
     * Receives a GET mapping to find and return the winner ballot of a date.
     * @param date the date to look for the winner.
     * @return ResponseEntity with the winner ballot if there was a completed lottery in the given date.
     * Otherwise, ResponseEntity with unprocessableEntity and the error message in the body.
     */
    @GetMapping(path = "/lottery/winner")
    public ResponseEntity<?> getWinner (@RequestBody @DateTimeFormat(pattern= "yyyy-MM-dd")LocalDate date){
        try {
            return ResponseEntity.ok(service.getWinner(date));
        }
        catch (Exception e) {
            return ResponseEntity.unprocessableEntity()
                    .body("No winner.");
        }
    }
}
