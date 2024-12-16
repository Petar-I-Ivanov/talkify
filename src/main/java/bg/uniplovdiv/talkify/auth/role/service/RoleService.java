package bg.uniplovdiv.talkify.auth.role.service;

import bg.uniplovdiv.talkify.auth.role.model.Role;
import bg.uniplovdiv.talkify.auth.role.model.RoleName;

public interface RoleService {

  Role getRoleByName(RoleName name);
}
