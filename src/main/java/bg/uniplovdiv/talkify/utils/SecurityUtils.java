package bg.uniplovdiv.talkify.utils;

import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.NOT_PERMITTED_EXC;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.common.models.CustomAccessDenyException;
import bg.uniplovdiv.talkify.security.CustomUserDetails;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;

@NoArgsConstructor(access = PRIVATE)
public class SecurityUtils {

  public static final String SPEL_REFERENCE = "T(bg.uniplovdiv.talkify.utils.SecurityUtils)";

  public static final Long fetchUserId() {
    return getCustomerUserDetails().map(CustomUserDetails::getId).orElse(-1L);
  }

  public static final String fetchPrincipal() {
    return getAuthenticatedAuthentication().map(Authentication::getName).orElse("anonymous");
  }

  public static final boolean isAuthenticated() {
    return getAuthenticatedAuthentication().isPresent();
  }

  public static final boolean isPermitted(Long channelId, String channelPermission) {
    var permission = channelId + ":" + channelPermission;
    return isPermitted(permission);
  }

  public static final boolean hasAnyPermissions(String... permissions) {
    return Stream.of(permissions).anyMatch(SecurityUtils::isPermitted);
  }

  public static final boolean hasAllPermissions(String... permissions) {
    return Stream.of(permissions).allMatch(SecurityUtils::isPermitted);
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

  public static final boolean hasRole(String role) {
    return getCustomerUserDetails().map(CustomUserDetails::getRoles).stream()
        .flatMap(Collection::stream)
        .anyMatch(role::equals);
  }

  public static final void revalidateUser(User user) {
    if (!isAuthenticated()) {
      return;
    }
    Optional.ofNullable(user)
        .map(CustomUserDetails::new)
        .map(CustomUserDetails::toAuthentication)
        .ifPresent(getContext()::setAuthentication);
  }

  public static final void throwIfNotAllowed(boolean isAllowed) {
    if (!isAllowed) {
      throw new CustomAccessDenyException(NOT_PERMITTED_EXC);
    }
  }

  private static Optional<Authentication> getAuthentication() {
    return Optional.ofNullable(getContext()).map(SecurityContext::getAuthentication);
  }

  private static Optional<Authentication> getAuthenticatedAuthentication() {
    return getAuthentication().filter(Authentication::isAuthenticated);
  }

  private static Optional<CustomUserDetails> getCustomerUserDetails() {
    return getAuthenticatedAuthentication()
        .map(Authentication::getPrincipal)
        .filter(CustomUserDetails.class::isInstance)
        .map(CustomUserDetails.class::cast);
  }
}
