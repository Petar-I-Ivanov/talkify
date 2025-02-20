package bg.uniplovdiv.talkify.auth.permission.model;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.role.model.Role;
import bg.uniplovdiv.talkify.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Permission extends BaseEntity {

  @Column(length = 255, nullable = false)
  String name;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "role_id", nullable = false)
  Role role;
}
