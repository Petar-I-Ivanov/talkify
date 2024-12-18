package bg.uniplovdiv.talkify.auth.role.service;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.role.model.Role;
import bg.uniplovdiv.talkify.auth.role.model.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

  RoleRepository roleRepository;

  @Override
  public Role getByName(String name) {
    return roleRepository.findByName(name);
  }
}
