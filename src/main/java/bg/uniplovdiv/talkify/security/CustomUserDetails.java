package bg.uniplovdiv.talkify.security;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.security.Principal;
import java.util.Collection;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
@FieldDefaults(level = PRIVATE)
public class CustomUserDetails implements UserDetails, Principal {

  private static final long serialVersionUID = -5581328326208200964L;

  Long id;

  String username;

  String email;

  @JsonIgnore String password;

  @JsonIgnore Collection<String> roles;

  @JsonIgnore Collection<? extends GrantedAuthority> authorities;

  boolean enabled;

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public String getName() {
    return username;
  }
}
