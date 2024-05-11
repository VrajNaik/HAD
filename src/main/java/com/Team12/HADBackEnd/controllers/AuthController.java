package com.Team12.HADBackEnd.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.Team12.HADBackEnd.DTOs.Token.TokenRefreshRequest;
import com.Team12.HADBackEnd.DTOs.Token.TokenRefreshResponse;
import com.Team12.HADBackEnd.DTOs.auth.*;
import com.Team12.HADBackEnd.exception.TokenRefreshException;
import com.Team12.HADBackEnd.models.*;
import com.Team12.HADBackEnd.payload.exception.CustomErrorResponse;
import com.Team12.HADBackEnd.payload.exception.NotFoundException;
import com.Team12.HADBackEnd.payload.exception.UserDeactivatedException;
import com.Team12.HADBackEnd.payload.response.*;
import com.Team12.HADBackEnd.repository.*;
import com.Team12.HADBackEnd.security.services.*;
import com.Team12.HADBackEnd.services.Common.ForgotPasswordService;
import com.Team12.HADBackEnd.services.Doctor.DoctorService;
import com.Team12.HADBackEnd.services.FieldHealthCareWorker.FieldHealthCareWorkerService;
import com.Team12.HADBackEnd.services.Supervisor.SupervisorService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Team12.HADBackEnd.security.jwt.JwtUtils;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final DoctorRepository doctorRepository;

  private final SupervisorRepository supervisorRepository;

  private final FieldHealthCareWorkerRepository fieldHealthcareWorkerRepository;

  private final DoctorService doctorService;

  private final SupervisorService supervisorService;

  private final FieldHealthCareWorkerService fieldHealthCareWorkerService;

  private final ForgotPasswordService forgotPasswordService;

  private final RefreshTokenService refreshTokenService;

  private final PasswordEncoder encoder;

  private final JwtUtils jwtUtils;

  @Autowired
  public AuthController(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        RoleRepository roleRepository,
                        DoctorRepository doctorRepository,
                        SupervisorRepository supervisorRepository,
                        FieldHealthCareWorkerRepository fieldHealthCareWorkerRepository,
                        DoctorService doctorService,
                        SupervisorService supervisorService,
                        FieldHealthCareWorkerService fieldHealthCareWorkerService,
                        ForgotPasswordService forgotPasswordService,
                        RefreshTokenService refreshTokenService,
                        PasswordEncoder encoder,
                        JwtUtils jwtUtils) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.doctorRepository = doctorRepository;
    this.supervisorRepository = supervisorRepository;
    this.fieldHealthcareWorkerRepository = fieldHealthCareWorkerRepository;
    this.fieldHealthCareWorkerService = fieldHealthCareWorkerService;
    this.doctorService = doctorService;
    this.supervisorService = supervisorService;
    this.forgotPasswordService = forgotPasswordService;
    this.refreshTokenService = refreshTokenService;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
  }


//  @PostMapping("/signin")
//  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
//    try {
//      Authentication authentication = authenticationManager.authenticate(
//              new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
//
//      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//
//      if (!userDetails.isActivate()) {
//        throw new UserDeactivatedException("Sorry, you are deactivated by the Admin. Contact the Admin for further assistance.");
//      }
//      SecurityContextHolder.getContext().setAuthentication(authentication);
//      String jwt = jwtUtils.generateJwtToken(authentication);
//      Object userRole = null;
//      boolean isAdmin = userDetails.getAuthorities().stream()
//              .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
//      if (isAdmin) {
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(new AuthResponseDTO(
//                new JwtResponseDTO(jwt,
//                        userDetails.getId(),
//                        userDetails.getUsername(),
//                        userDetails.getEmail(),
//                        roles,
//                        userDetails.isLogInFirst()
//                        ),
//                 null));
//      }
//      else {
//        String role = null;
//        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"))) {
//          role = "doctor";
//          Doctor doctor = doctorRepository.findByUsername(userDetails.getUsername())
//                  .orElseThrow(() -> new RuntimeException("Error: DOCTOR role not found."));
//          userRole = doctorService.convertToDTO(doctor);
//        }
//        else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_SUPERVISOR"))) {
//          role = "supervisor";
//          Supervisor supervisor = supervisorRepository.findByUsername(userDetails.getUsername())
//                  .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR role not found."));
//          userRole = supervisorService.convertToDTO(supervisor);
//        } else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_FIELD_HEALTHCARE_WORKER"))) {
//          role = "fieldHealthcareWorker";
//          FieldHealthCareWorker fieldHealthCareWorker = fieldHealthcareWorkerRepository.findByUsername(userDetails.getUsername())
//                  .orElseThrow(() -> new RuntimeException("Error: FIELD HEALTH CARE WORKER role not found."));
//          userRole = fieldHealthCareWorkerService.convertToFieldHealthCareWorkerWithHealthRecordDTO(fieldHealthCareWorker);
//        }
//        return ResponseEntity.ok(new AuthResponseDTO(
//                new JwtResponseDTO(jwt,
//                        userDetails.getId(),
//                        userDetails.getUsername(),
//                        userDetails.getEmail(),
//                        Collections.singletonList(role),
//                        userDetails.isLogInFirst()
//                        ),
//                userRole
//        ));
//      }
//    } catch (UserDeactivatedException e) {
//      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//    }
//  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      if (!userDetails.isActivate()) {
        throw new NotFoundException("Sorry, you are deactivated by the Admin. Contact the Admin for further assistance.");
      }

      SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = jwtUtils.generateJwtToken(authentication);
      Object userRole = null;
      boolean isAdmin = userDetails.getAuthorities().stream()
              .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
      if (isAdmin) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new AuthResponseDTO(
                new JwtResponseDTO(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        userDetails.isLogInFirst()
                ),
                null));
      } else {
        String role = null;
        boolean isActive = true; // Assuming user is active by default
        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_DOCTOR"))) {
          role = "doctor";
          Doctor doctor = doctorRepository.findByUsername(userDetails.getUsername())
                  .orElseThrow(() -> new RuntimeException("Error: DOCTOR role not found."));
          isActive = doctor.isActive();
          userRole = doctorService.convertToDTO(doctor);
        } else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_SUPERVISOR"))) {
          role = "supervisor";
          Supervisor supervisor = supervisorRepository.findByUsername(userDetails.getUsername())
                  .orElseThrow(() -> new RuntimeException("Error: SUPERVISOR role not found."));
          isActive = supervisor.isActive();
          userRole = supervisorService.convertToDTO(supervisor);
        } else if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_FIELD_HEALTHCARE_WORKER"))) {
          role = "fieldHealthcareWorker";
          FieldHealthCareWorker fieldHealthCareWorker = fieldHealthcareWorkerRepository.findByUsername(userDetails.getUsername())
                  .orElseThrow(() -> new RuntimeException("Error: FIELD HEALTH CARE WORKER role not found."));
          isActive = fieldHealthCareWorker.isActive();
          userRole = fieldHealthCareWorkerService.convertToFieldHealthCareWorkerWithHealthRecordDTO(fieldHealthCareWorker);
        }
        if (!isActive) {
          throw new NotFoundException("Sorry, your role as a " + role + " is deactivated by the Admin. Contact the Admin for further assistance.");
        }
        return ResponseEntity.ok(new AuthResponseDTO(
                new JwtResponseDTO(jwt,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        Collections.singletonList(role),
                        userDetails.isLogInFirst()
                ),
                userRole
        ));
      }
    } catch (UserDeactivatedException e) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
  }


  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUser)
            .map(user -> {
              String token = jwtUtils.generateTokenFromUsername(user.getUsername());
              return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
            })
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                    "Refresh token is not in database!"));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequestDTO) {
    if (userRepository.existsByUsername(signUpRequestDTO.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    User user = new User(signUpRequestDTO.getUsername(),
               signUpRequestDTO.getEmail(),
               encoder.encode(signUpRequestDTO.getPassword()));

    Set<String> strRoles = signUpRequestDTO.getRole();
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
                    .orElseThrow(() -> new RuntimeException("Error: FIELD HEALTHCARE WORKER is not found."));
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

  @PostMapping("/forgotPassword")
  public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
    String email = forgotPasswordRequestDTO.getEmail();
    User user = userRepository.findByEmail(email);
    if (user == null) {
      CustomErrorResponse errorResponse = new CustomErrorResponse("/api/forgot-password", "User Not Found", "User with email " + email + " not found.", 404);
      return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    forgotPasswordService.initiatePasswordReset(email);
    return ResponseEntity.ok("Password reset instructions sent to your email.");
  }

  @PostMapping("/resetPassword")
  public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
    String token = resetPasswordRequestDTO.getToken();
    String newPassword = resetPasswordRequestDTO.getNewPassword();
    boolean success = forgotPasswordService.resetPassword(token, newPassword);
    if (success) {
      return ResponseEntity.ok("Password reset successfully.");
    } else {
      CustomErrorResponse errorResponse = new CustomErrorResponse("/api/reset-password", "Invalid Token", "Invalid or expired token.", 400);
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping("/changePassword")
  public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
    String username = resetPasswordRequestDTO.getToken();
    String newPassword = resetPasswordRequestDTO.getNewPassword();
    boolean success = forgotPasswordService.changePassword(username, newPassword);
    if (success) {
      return ResponseEntity.ok("Password reset successfully.");
    } else {
      CustomErrorResponse errorResponse = new CustomErrorResponse("/api/change-password", "Invalid Token", "Invalid or expired token.", 400);
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
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





//
//        Map<String, Long> counts = new HashMap<>();
//        counts.put("doctors", doctorCount);
//        counts.put("supervisors", supervisorCount);
//        counts.put("fieldHealthcareWorkers", fieldWorkerCount);
//        counts.put("citizens", citizen);

//        // Count only active doctors, supervisors, and field healthcare workers
//        long doctorCount = doctorRepository.countByActiveTrue();
//        long supervisorCount = supervisorRepository.countByActiveTrue();
//        long fieldWorkerCount = fieldHealthcareWorkerRepository.countByActiveTrue();
//        long citizen = citizenRepository.count();