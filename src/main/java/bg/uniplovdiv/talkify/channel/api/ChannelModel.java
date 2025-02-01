package bg.uniplovdiv.talkify.channel.api;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "channels")
@FieldDefaults(level = PRIVATE)
public class ChannelModel extends RepresentationModel<ChannelModel> {

  @EncodedId Long id;
  String name;
}
