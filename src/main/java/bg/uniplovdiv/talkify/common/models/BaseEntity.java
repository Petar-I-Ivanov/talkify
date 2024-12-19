package bg.uniplovdiv.talkify.common.models;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@MappedSuperclass
@Getter
@Setter
@FieldDefaults(level = PROTECTED)
public class BaseEntity {

  @Id @GeneratedValue Long id;

  boolean active;
}
