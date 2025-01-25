package bg.uniplovdiv.talkify.config;

import static jakarta.servlet.DispatcherType.FORWARD;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Optional;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableMethodSecurity
@FieldDefaults(level = PRIVATE)
public class SecurityConfiguration {

  static final String APIS = "/api/**";

  @Value("${application.headers.content-security-policy:#{null}}")
  String contentSecurityPolicy;

  @Order(1)
  @Bean
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    return http.securityMatcher(APIS)
        .requestCache(withDefaults())
        .cors(CorsConfigurer::disable)
        .authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers(permittedApiRequests())
                    .permitAll()
                    .requestMatchers(APIS)
                    .fullyAuthenticated())
        .build();
  }

  @Order(2)
  @Bean
  public SecurityFilterChain htmlFilterChain(
      HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
    return http.csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
        .headers(
            headers ->
                headers
                    .frameOptions(FrameOptionsConfig::deny)
                    .contentSecurityPolicy(
                        csp ->
                            Optional.ofNullable(contentSecurityPolicy)
                                .map(csp::policyDirectives)
                                .orElse(csp)))
        .requestCache(withDefaults())
        .cors(CorsConfigurer::disable)
        .authorizeHttpRequests(
            requests ->
                requests
                    .dispatcherTypeMatchers(FORWARD)
                    .permitAll()
                    .requestMatchers(permittedHtmlRequests())
                    .permitAll()
                    .anyRequest()
                    .fullyAuthenticated())
        .logout(
            logout ->
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", GET.name()))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutSuccessUrl("/sign-in")
                    .deleteCookies("JSESSIONID", "X-CSRF-TOKEN")
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

  private static RequestMatcher[] permittedApiRequests() {
    return new RequestMatcher[] {
      new AntPathRequestMatcher("/api/v1/users/register", POST.name()),
      new AntPathRequestMatcher("/api/v1/users/exists/username", GET.name()),
      new AntPathRequestMatcher("/api/v1/users/exists/email", GET.name())
    };
  }

  private static String[] permittedHtmlRequests() {
    return new String[] {
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/ws/**",
      "/error",
      "/csrf-token",
      "/sign-in",
      "/sign-up",
      "/login",
      "/static/**",
      "/assets/**",
      "/h2-console/**",
      "/favicon.ico"
    };
  }
}
