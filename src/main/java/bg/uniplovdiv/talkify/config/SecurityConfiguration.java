package bg.uniplovdiv.talkify.config;

import static jakarta.servlet.DispatcherType.FORWARD;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfiguration {

  private static String APIS = "/api/**";

  @Order(1)
  @Bean
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    return http.securityMatcher(APIS)
        .requestCache(Customizer.withDefaults())
        .csrf(CsrfConfigurer::disable)
        .authorizeHttpRequests(requests -> requests.requestMatchers(APIS).authenticated())
        .build();
  }

  @Order(2)
  @Bean
  public SecurityFilterChain htmlFilterChain(
      HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
    return http.csrf(csfr -> csfr.ignoringRequestMatchers("/login", "/logout"))
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
        .requestCache(Customizer.withDefaults())
        .authorizeHttpRequests(
            requests ->
                requests
                    .dispatcherTypeMatchers(FORWARD)
                    .permitAll()
                    .requestMatchers(
                        "/", "/sign-in", "/sign-up", "/login", "/static/**", "/assets/**")
                    .permitAll()
                    .anyRequest()
                    .fullyAuthenticated())
        .logout(
            logout ->
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/")
                    .deleteCookies("JSESSIONID")
                    .permitAll())
        .formLogin(
            formLogin ->
                formLogin
                    .loginPage("/sign-in")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/", true)
                    .failureUrl("/sign-in?error=true"))
        .authenticationProvider(authenticationProvider)
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider daoAuthenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    var authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);
    return authProvider;
  }
}
