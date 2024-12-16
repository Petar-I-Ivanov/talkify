package bg.uniplovdiv.talkify.auth.role.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.permission.model.Permission;
import bg.uniplovdiv.talkify.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
public class Role extends BaseEntity {

  @Column(length = 100, nullable = false, unique = true)
  @Enumerated(STRING)
  RoleName name;

  @Builder.Default
  @OneToMany(mappedBy = "role", cascade = ALL, orphanRemoval = true)
  Set<Permission> permissions = new LinkedHashSet<>();
}
