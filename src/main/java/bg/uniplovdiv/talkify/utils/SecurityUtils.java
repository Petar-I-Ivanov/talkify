package bg.uniplovdiv.talkify.utils;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import bg.uniplovdiv.talkify.security.CustomUserDetails;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.User;

@NoArgsConstructor(access = PRIVATE)
public class SecurityUtils {

  public static final String fetchPrincipal() {
    return getAuthenticatedAuthentication()
        .map(Authentication::getPrincipal)
        .map(SecurityUtils::resolvePrincipal)
        .orElse("anonymous");
  }

  public static final boolean isAuthenticated() {
    return getAuthenticatedAuthentication().isPresent();
  }

  public static final boolean isPermitted(String permission) {
    return getAuthenticatedAuthentication()
        .map(Authentication::getAuthorities)
        .map(authorities -> isPermitted(permission, authorities))
        .orElse(false);
  }

  public static final boolean isPermitted(
      String permission, Collection<? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .map(GrantedAuthority::getAuthority)
        .anyMatch(
            auth ->
                auth.contains(",")
                    ? Stream.of(permission.split(":")).allMatch(auth::contains)
                    : permission.matches(auth.replace("*", ".*")));
  }

  private static final Optional<Authentication> getAuthentication() {
    return Optional.ofNullable(getContext()).map(SecurityContext::getAuthentication);
  }

  private static final Optional<Authentication> getAuthenticatedAuthentication() {
    return getAuthentication().filter(Authentication::isAuthenticated);
  }

  private static final String resolvePrincipal(Object principal) {
    if (principal instanceof CustomUserDetails details) {
      return details.getUsername();
    }

    if (principal instanceof User user) {
      return user.getUsername();
    }

    return principal.toString();
  }
}