package bg.uniplovdiv.talkify.auth.user.model;

import static jakarta.persistence.FetchType.LAZY;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.collections4.SetUtils.union;

import bg.uniplovdiv.talkify.auth.permission.model.ChannelPermissions;
import bg.uniplovdiv.talkify.auth.permission.model.Permission;
import bg.uniplovdiv.talkify.auth.role.model.Role;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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

  @Column(length = 64, nullable = false, unique = true)
  String username;

  @Column(length = 150, nullable = false, unique = true)
  String email;

  @Column(length = 150, nullable = false)
  String password;

  @Builder.Default
  @ManyToMany(fetch = LAZY)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  Set<Role> roles = new LinkedHashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "owner", orphanRemoval = true, fetch = LAZY)
  Set<Channel> ownedChannels = new LinkedHashSet<>();

  @Builder.Default
  @ManyToMany(mappedBy = "admins", fetch = LAZY)
  Set<Channel> adminChannels = new LinkedHashSet<>();

  @Builder.Default
  @ManyToMany(mappedBy = "guests", fetch = LAZY)
  Set<Channel> guestChannels = new LinkedHashSet<>();

  public Set<String> getAllPermissions() {
    return union(getRolePermissions(), getChannelPermissions());
  }

  private Set<String> getRolePermissions() {
    return roles.stream()
        .map(Role::getPermissions)
        .flatMap(Set::stream)
        .map(Permission::getValue)
        .collect(toSet());
  }

  private Set<String> getChannelPermissions() {
    var permissions = new LinkedHashSet<String>();
    permissions.addAll(getOwnedChannelPermissions());
    permissions.addAll(getAdminChannelPermissions());
    permissions.addAll(getGuestChannelPermissions());
    return permissions;
  }

  private Set<String> getOwnedChannelPermissions() {
    return ownedChannels.stream()
        .map(ChannelPermissions::buildOwnedChannelPermissions)
        .collect(toSet());
  }

  private Set<String> getAdminChannelPermissions() {
    return adminChannels.stream()
        .map(ChannelPermissions::buildAdminChannelPermissions)
        .collect(toSet());
  }

  private Set<String> getGuestChannelPermissions() {
    return guestChannels.stream()
        .map(ChannelPermissions::buildGuestChannelPermissions)
        .collect(toSet());
  }
}
