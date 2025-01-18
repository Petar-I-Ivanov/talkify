package bg.uniplovdiv.talkify.channel.model;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.common.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class Channel extends BaseEntity {

  @Column(length = 255, nullable = false, unique = true)
  String name;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "owner_id", nullable = false)
  User owner;

  @Builder.Default
  @ManyToMany(fetch = LAZY)
  @JoinTable(
      name = "channel_admin",
      joinColumns = @JoinColumn(name = "channel_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"))
  Set<User> admins = new LinkedHashSet<>();

  @Builder.Default
  @ManyToMany(fetch = LAZY)
  @JoinTable(
      name = "channel_guest",
      joinColumns = @JoinColumn(name = "channel_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "guest_id", referencedColumnName = "id"))
  Set<User> guests = new LinkedHashSet<>();

  public boolean isUserAlreadyInChannel(Long userId) {
    return owner.getId().equals(userId)
        || admins.stream().map(User::getId).anyMatch(userId::equals)
        || guests.stream().map(User::getId).anyMatch(userId::equals);
  }
}
