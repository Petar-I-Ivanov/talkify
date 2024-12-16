package bg.uniplovdiv.talkify.auth.user.model;

import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.permission.model.Permission;
import bg.uniplovdiv.talkify.auth.permission.model.PermissionValues;
import bg.uniplovdiv.talkify.auth.role.model.Role;
import bg.uniplovdiv.talkify.auth.role.model.RoleName;
import bg.uniplovdiv.talkify.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class User extends BaseEntity {

  String username;

  String email;

  String password;

  @Builder.Default
  @ManyToMany
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  Set<Role> roles = new LinkedHashSet<>();

  // @Builder.Default
  // @OneToMany(mappedBy = "owner")
  // Set<Channel> ownedChannels = new LinkedHashSet<>();

  // @Builder.Default
  // @ManyToMany(mappedBy = "admins")
  // Set<Channel> adminChannels = new LinkedHashSet<>();

  // @Builder.Default
  // @ManyToMany(mappedBy = "guests")
  // Set<Channel> guestChannels = new LinkedHashSet<>();

  public boolean hasRole(RoleName role) {
    return roles.stream().map(Role::getName).anyMatch(role::equals);
  }

  public Set<String> getPermissions() {
    return roles.stream()
        .map(Role::getPermissions)
        .flatMap(Set::stream)
        .map(Permission::getValue)
        .map(PermissionValues::getValue)
        .collect(toSet());
  }
}
