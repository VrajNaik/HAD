package com.Team12.HADBackEnd.config;


import com.Team12.HADBackEnd.security.jwt.AuthEntryPointJwt;
import com.Team12.HADBackEnd.security.jwt.AuthTokenFilter;
import com.Team12.HADBackEnd.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableMethodSecurity
// (securedEnabled = true,
// jsr250Enabled = true,
// prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth ->
                    auth.requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/**").permitAll()
                            .requestMatchers("/test/**").permitAll()
                            .requestMatchers("/admin/**").permitAll()
                            .requestMatchers("/doctor/viewDoctors").permitAll()
                            .requestMatchers("/doctor/addDoctor").permitAll()
                            .requestMatchers("/doctor/updateDoctor").permitAll()
                            .requestMatchers("/doctor/deactivate").permitAll()
                            .requestMatchers("/district/all").permitAll()
                            .requestMatchers("/district/create").permitAll()
                            .requestMatchers("/swagger-ui/index.html").permitAll()
                            .requestMatchers("/v3/api-docs").permitAll()
                            .requestMatchers("/district/unallocated").permitAll()
                            .requestMatchers("/api/forgot-password").permitAll()
                            .requestMatchers("/api/reset-password").permitAll()
                            .requestMatchers("/district/getlocalareaswithindistrict").permitAll()
                            .requestMatchers("/district/**").permitAll()
                            .requestMatchers("/doctor/**").permitAll()
                            .requestMatchers("/supervisor/**").permitAll()
                            .requestMatchers("/FieldHealthCareWorker/**").permitAll()
                            .requestMatchers("/api/**").permitAll()
                            .requestMatchers("/api/translate/**").permitAll()

                            .anyRequest().authenticated()
            );

   // http.authenticationProvider(authenticationProvider());

    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}

//  @Override
//  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//  }

//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new CustomPasswordEncoder();
//  }

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors().and().csrf().disable()
//      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//      .antMatchers("/api/test/**").permitAll()
//      .anyRequest().authenticated();
//
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }


//  @Bean
//  @Override
//  public AuthenticationManager authenticationManagerBean() throws Exception {
//    return super.authenticationManagerBean();
//  }