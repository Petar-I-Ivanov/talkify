package bg.uniplovdiv.talkify.security;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.authenticated;

import bg.uniplovdiv.talkify.auth.role.model.Role;
import bg.uniplovdiv.talkify.auth.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.security.Principal;
import java.util.Collection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@NoArgsConstructor
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

  public CustomUserDetails(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.enabled = user.isActive();
    this.roles = user.getRoles().stream().map(Role::getName).collect(toList());
    this.authorities =
        user.getAllPermissions().stream().map(SimpleGrantedAuthority::new).collect(toSet());
  }

  public Authentication toAuthentication() {
    return authenticated(this, this.getUsername(), this.getAuthorities());
  }
}
