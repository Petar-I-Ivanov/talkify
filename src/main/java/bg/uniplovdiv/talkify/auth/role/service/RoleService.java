package bg.uniplovdiv.talkify.auth.role.service;

import bg.uniplovdiv.talkify.auth.role.model.Role;

public interface RoleService {

  Role getRoleByName(String name);
}
