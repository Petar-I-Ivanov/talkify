package bg.uniplovdiv.talkify.security;

import bg.uniplovdiv.talkify.utils.SecurityUtils;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

  public boolean hasAllPermissions(String... permissions) {
    return Stream.of(permissions).allMatch(SecurityUtils::isPermitted);
  }

  public boolean hasAnyPermissions(String... permissions) {
    return Stream.of(permissions).anyMatch(SecurityUtils::isPermitted);
  }
}
