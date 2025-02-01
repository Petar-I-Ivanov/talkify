package bg.uniplovdiv.talkify.message.api;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "messages")
@FieldDefaults(level = PRIVATE)
public class MessageModel extends RepresentationModel<MessageModel> {

  @EncodedId Long id;
  String text;
  String sender;
  LocalDateTime sentAt;
  LocalDateTime editedAt;
}
