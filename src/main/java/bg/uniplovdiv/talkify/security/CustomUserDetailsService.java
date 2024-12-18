package bg.uniplovdiv.talkify.security;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {

  UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userService
        .getByUsernameOrEmail(username)
        .map(CustomUserDetailsService::userToUserDetails)
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username:" + username));
  }

  private static final UserDetails userToUserDetails(User user) {
    return CustomUserDetails.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .password(user.getPassword())
        .enabled(user.isActive())
        .authorities(
            user.getAllPermissions().stream().map(SimpleGrantedAuthority::new).collect(toSet()))
        .build();
  }
}
