package com.Team12.HADBackEnd.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.Team12.HADBackEnd.models.ERole;
import com.Team12.HADBackEnd.models.Role;
import com.Team12.HADBackEnd.models.User;
import com.Team12.HADBackEnd.payload.request.LoginRequest;
import com.Team12.HADBackEnd.payload.request.SignupRequest;
import com.Team12.HADBackEnd.payload.response.AuthResponse;
import com.Team12.HADBackEnd.payload.response.JwtResponse;
import com.Team12.HADBackEnd.payload.response.MessageResponse;
import com.Team12.HADBackEnd.payload.response.UserDeactivatedException;
import com.Team12.HADBackEnd.repository.*;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team12.HADBackEnd.security.jwt.JwtUtils;
import com.Team12.HADBackEnd.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
  @Autowired
  SupervisorRepository supervisorRepository;

  @Autowired
  DoctorRepository doctorRepository;

  @Autowired
  FieldHealthCareWorkerRepository fieldHealthcareWorkerRepository;

//  @PostMapping("/signin")
//  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//    try {
//      Authentication authentication = authenticationManager.authenticate(
//              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//      if (!userDetails.isActivate()) {
//        throw new UserDeactivatedException("Sorry, you are deactivated by the Admin. Contact the Admin for further assistance.");
//      }
//
//      SecurityContextHolder.getContext().setAuthentication(authentication);
//      String jwt = jwtUtils.generateJwtToken(authentication);
//
//      long supervisorCount = supervisorRepository.countByActiveTrue(); // Count only active supervisors
//      long doctorCount = doctorRepository.countByActiveTrue(); // Count only active doctors
//      long fieldWorkerCount = fieldHealthcareWorkerRepository.countByActiveTrue(); // Count only active field healthcare workers
//
//
//      Map<String, Long> counts = new HashMap<>();
//      counts.put("supervisors", supervisorCount);
//      counts.put("doctors", doctorCount);
//      counts.put("fieldHealthcareWorkers", fieldWorkerCount);
//
//      List<String> roles = userDetails.getAuthorities().stream()
//              .map(item -> item.getAuthority())
//              .collect(Collectors.toList());
//
//      return ResponseEntity.ok(new AuthResponse(
//              new JwtResponse(jwt,
//                      userDetails.getId(),
//                      userDetails.getUsername(),
//                      userDetails.getEmail(),
//                      roles),
//              counts
//      ));
//    } catch (UserDeactivatedException e) {
//      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//    }
//  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      if (!userDetails.isActivate()) {
        throw new UserDeactivatedException("Sorry, you are deactivated by the Admin. Contact the Admin for further assistance.");
      }

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);

      // Check if the user is an admin
      boolean isAdmin = userDetails.getAuthorities().stream()
              .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

      if (isAdmin) {
        // Count only active doctors, supervisors, and field healthcare workers
        long doctorCount = doctorRepository.countByActiveTrue();
        long supervisorCount = supervisorRepository.countByActiveTrue();
        long fieldWorkerCount = fieldHealthcareWorkerRepository.countByActiveTrue();

        Map<String, Long> counts = new HashMap<>();
        counts.put("doctors", doctorCount);
        counts.put("supervisors", supervisorCount);
        counts.put("fieldHealthcareWorkers", fieldWorkerCount);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AuthResponse(
                new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles),
                counts,
                null));
      } else {
        // Fetch user's role and count from respective table
        String role = null;
        long roleCount = 0;
        Object userDetail = null;

        // Fetch the user's role based on their username/email
        // Fetch user details based on their role
        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"))) {
          role = "doctor";
          roleCount = doctorRepository.countByActiveTrue();
          userDetail = doctorRepository.findByUsername(userDetails.getUsername()); // Fetch doctor details
        } else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_SUPERVISOR"))) {
          role = "supervisor";
          roleCount = supervisorRepository.countByActiveTrue();
          userDetail = supervisorRepository.findByUsername(userDetails.getUsername()); // Fetch supervisor details
        } else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_FIELD_HEALTHCARE_WORKER"))) {
          role = "fieldHealthcareWorker";
          roleCount = fieldHealthcareWorkerRepository.countByActiveTrue();
          userDetail = fieldHealthcareWorkerRepository.findByUsername(userDetails.getUsername()); // Fetch field healthcare worker details
        }

        // Return the response with the user's role, count, and details
        return ResponseEntity.ok(new AuthResponse(
                new JwtResponse(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        Collections.singletonList(role)), // Return user's role
                Collections.singletonMap(role, roleCount), // Return role count
                userDetail // Return user's details
        ));
      }
    } catch (UserDeactivatedException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
               signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
          .orElseThrow(() -> new RuntimeException("Error: DOCTOR is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "supervisor":
            Role modRole = roleRepository.findByName(ERole.ROLE_SUPERVISOR)
                    .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR is not found."));
            roles.add(modRole);

            break;
          case "doctor":
            Role docRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                    .orElseThrow(() -> new RuntimeException("Error: DOCTOR is not found."));
            roles.add(docRole);

            break;
          case "fieldhealthcareworker":
            Role fhwRole = roleRepository.findByName(ERole.ROLE_FIELD_HEALTHCARE_WORKER)
                    .orElseThrow(() -> new RuntimeException("Error: FEILD HEALTHCARE WORKER is not found."));
            roles.add(fhwRole);

            break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}

//  @PostMapping("/signin")
//  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
//
//    Authentication authentication = authenticationManager.authenticate(
//        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//    SecurityContextHolder.getContext().setAuthentication(authentication);
//    String jwt = jwtUtils.generateJwtToken(authentication);
//
//    long supervisorCount = supervisorRepository.count();
//    long doctorCount = doctorRepository.count();
//    long fieldWorkerCount = fieldHealthcareWorkerRepository.count();
//
//    Map<String, Long> counts = new HashMap<>();
//    counts.put("supervisors", supervisorCount);
//    counts.put("doctors", doctorCount);
//    counts.put("fieldHealthcareWorkers", fieldWorkerCount);
//
//    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//    List<String> roles = userDetails.getAuthorities().stream()
//        .map(item -> item.getAuthority())
//        .collect(Collectors.toList());
//
////    return ResponseEntity.ok(new JwtResponse(jwt,
////                         userDetails.getId(),
////                         userDetails.getUsername(),
////                         userDetails.getEmail(),
////                         roles));
//    return ResponseEntity.ok(new AuthResponse(
//            new JwtResponse(jwt,
//                    userDetails.getId(),
//                    userDetails.getUsername(),
//                    userDetails.getEmail(),
//                    roles),
//            counts
//    ));
//  }
//@PostMapping("/signin")
//public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws UserDeactivatedException{
//  Authentication authentication = authenticationManager.authenticate(
//          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
//
//  UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//  if (!userDetails.isActivate()) {
//    throw new UserDeactivatedException("Sorry, you are deactivated by the Admin. Contact the Admin for further assistance.");
//  }
//
//  SecurityContextHolder.getContext().setAuthentication(authentication);
//  String jwt = jwtUtils.generateJwtToken(authentication);
//
//  long supervisorCount = supervisorRepository.count();
//  long doctorCount = doctorRepository.count();
//  long fieldWorkerCount = fieldHealthcareWorkerRepository.count();
//
//  Map<String, Long> counts = new HashMap<>();
//  counts.put("supervisors", supervisorCount);
//  counts.put("doctors", doctorCount);
//  counts.put("fieldHealthcareWorkers", fieldWorkerCount);
//
//  List<String> roles = userDetails.getAuthorities().stream()
//          .map(item -> item.getAuthority())
//          .collect(Collectors.toList());
//
//  return ResponseEntity.ok(new AuthResponse(
//          new JwtResponse(jwt,
//                  userDetails.getId(),
//                  userDetails.getUsername(),
//                  userDetails.getEmail(),
//                  roles),
//          counts
//  ));
//}

//      long supervisorCount = supervisorRepository.count();
//      long doctorCount = doctorRepository.count();
//      long fieldWorkerCount = fieldHealthcareWorkerRepository.count();

//@CrossOrigin("http://172.16.145.87")
//@CrossOrigin("http://172.16.145.87")
