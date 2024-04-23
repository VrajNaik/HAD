package com.Team12.HADBackEnd.DTOs.auth;

import java.util.List;

public class JwtResponseDTO {
  private String token;
//  private String refreshToken;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String email;
  private List<String> roles;
  private boolean logInFirst;


  public JwtResponseDTO(String accessToken, /*String refreshToken ,*/ Long id, String username, String email, List<String> roles, boolean logInFirst) {
    this.token = accessToken;
//    this.refreshToken = refreshToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.roles = roles;
    this.logInFirst = logInFirst;

  }

//  public String getRefreshToken() {
//    return refreshToken;
//  }
//
//  public void setRefreshToken(String refreshToken) {
//    this.refreshToken = refreshToken;
//  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }

  public boolean isLogInFirst() {
    return logInFirst;
  }

  public void setLogInFirst(boolean logInFirst) {
    this.logInFirst = logInFirst;
  }
}
