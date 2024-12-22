package bg.uniplovdiv.talkify.auth.role.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.permission.model.Permission;
import bg.uniplovdiv.talkify.common.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Role extends BaseEntity {

  @Column(length = 100, nullable = false, unique = true)
  String name;

  @OneToMany(mappedBy = "role", orphanRemoval = true, cascade = ALL, fetch = LAZY)
  Set<Permission> permissions = new LinkedHashSet<>();
}
