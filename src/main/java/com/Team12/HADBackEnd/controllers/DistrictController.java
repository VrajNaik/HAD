package com.Team12.HADBackEnd.controllers;

import com.Team12.HADBackEnd.models.District;
import com.Team12.HADBackEnd.payload.request.CreateLocalAreasRequest;
import com.Team12.HADBackEnd.payload.request.DistrictDTO;
import com.Team12.HADBackEnd.payload.request.DistrictWithDoctorsDTO;
import com.Team12.HADBackEnd.security.services.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/district")
public class DistrictController {
    private final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService) {
        this.districtService = districtService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<DistrictWithDoctorsDTO>> getAllDistricts() {
        List<DistrictWithDoctorsDTO> districtDTOs = districtService.getAllDistricts();
        return ResponseEntity.ok(districtDTOs);
    }

//    @GetMapping("/unallocated")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<District>> getUnallocatedDistricts() {
//        List<District> unallocatedDistricts = districtService.getUnallocatedDistricts();
//        return ResponseEntity.ok(unallocatedDistricts);
//    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createDistrict(@RequestBody District district) {
        districtService.createDistrict(district);
        return ResponseEntity.ok("District created successfully");
    }
    @PostMapping("/createmany")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createDistricts(@RequestBody List<District> districts) {
        districtService.createDistricts(districts);
        return ResponseEntity.ok("Districts created successfully");
    }

    @PostMapping("/localareas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createLocalAreasInDistrict(@RequestBody CreateLocalAreasRequest request) {
        String message = districtService.createLocalAreasInDistrict(request);
        return ResponseEntity.ok(message);
    }
}
