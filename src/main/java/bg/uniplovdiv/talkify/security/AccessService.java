package bg.uniplovdiv.talkify.security;

import bg.uniplovdiv.talkify.utils.SecurityUtils;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

  public boolean hasPermission(String... permissions) {
    return Stream.of(permissions).allMatch(SecurityUtils::isPermitted);
  }

  public boolean hasRole(String... roles) {
    return Stream.of(roles).allMatch(SecurityUtils::hasRole);
  }
}
