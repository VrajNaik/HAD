package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.FieldHealthCareWorker;
import com.Team12.HADBackEnd.models.Supervisor;
import com.Team12.HADBackEnd.security.services.FieldHealthCareWorkerService;
import com.Team12.HADBackEnd.security.services.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/FieldHealthCareWorker")
public class FieldHealthCareWorkerController {

    @Autowired
    private FieldHealthCareWorkerService fieldHealthCareWorkerService;

    @PostMapping("/addFieldHealthCareWorker")
    @PreAuthorize("hasRole('ADMIN')")
    public FieldHealthCareWorker addFieldHealthCareWorker(@RequestBody FieldHealthCareWorker fieldHealthCareWorker) {
        return fieldHealthCareWorkerService.addFieldHealthCareWorker(fieldHealthCareWorker);
    }
    @GetMapping("/viewFieldHealthCareWorkers")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FieldHealthCareWorker>> getAllFieldHealthCareWorker() {
        List<FieldHealthCareWorker> fieldHealthCareWorker = fieldHealthCareWorkerService.getAllFieldHealthCareWorker();
        return new ResponseEntity<>(fieldHealthCareWorker, HttpStatus.OK);
    }
}



