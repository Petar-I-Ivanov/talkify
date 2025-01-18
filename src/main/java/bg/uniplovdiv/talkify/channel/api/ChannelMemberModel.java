package bg.uniplovdiv.talkify.channel.api;

import static lombok.AccessLevel.PRIVATE;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Builder
@Relation(collectionRelation = "channelMembers")
@FieldDefaults(level = PRIVATE)
public class ChannelMemberModel extends RepresentationModel<ChannelMemberModel> {

  Long id;
  String username;
  ChannelMemberRole role;
}
