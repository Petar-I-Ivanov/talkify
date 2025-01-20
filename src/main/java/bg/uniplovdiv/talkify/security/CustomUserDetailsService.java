package bg.uniplovdiv.talkify.security;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomUserDetailsService implements UserDetailsService {

  UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userService
        .getByUsernameOrEmail(username)
        .map(CustomUserDetails::new)
        .orElseThrow(
            () -> new UsernameNotFoundException("User not found with username:" + username));
  }
}
