package bg.uniplovdiv.talkify.auth.user.api;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "users")
@FieldDefaults(level = PRIVATE)
public class UserModel extends RepresentationModel<UserModel> {

  @EncodedId Long id;
  String username;
  String email;
  @EncodedId Long privateChannelId;
}
