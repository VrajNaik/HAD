package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.security.services.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/supervisor")
public class SupervisorController {

    @Autowired
    private SupervisorService supervisorService;

    @PostMapping("/addSupervisor")
    @PreAuthorize("hasRole('ADMIN')")
    public Supervisor addSupervisor(@RequestBody Supervisor supervisor) {
        return supervisorService.addSupervisor(supervisor);
    }
    @GetMapping("/viewSupervisors")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Supervisor>> getAllSupervisor() {
        List<Supervisor> supervisor = supervisorService.getAllSupervisor();
        return new ResponseEntity<>(supervisor, HttpStatus.OK);
    }
}


