package bg.uniplovdiv.talkify.channel.members.api;

import static bg.uniplovdiv.talkify.channel.members.api.ChannelMemberRole.ADMIN;
import static bg.uniplovdiv.talkify.channel.members.api.ChannelMemberRole.GUEST;
import static bg.uniplovdiv.talkify.channel.members.api.ChannelMemberRole.OWNER;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.service.ChannelService;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChannelMemberModelAssembler {

  ChannelService channelService;

  public Collection<ChannelMemberModel> toChannelMembers(Channel channel) {
    var owner = mapUserToMember(channel.getOwner(), OWNER, channel);
    var admins = mapToMembers(channel.getAdmins(), ADMIN, channel);
    admins.addFirst(owner);
    var guests = mapToMembers(channel.getGuests(), GUEST, channel);
    admins.addAll(guests);
    return admins;
  }

  private List<ChannelMemberModel> mapToMembers(
      Collection<User> members, ChannelMemberRole role, Channel channel) {
    return members.stream().map(user -> mapUserToMember(user, role, channel)).collect(toList());
  }

  private ChannelMemberModel mapUserToMember(User user, ChannelMemberRole role, Channel channel) {
    return ChannelMemberModel.builder()
        .id(user.getId())
        .username(user.getUsername())
        .role(role)
        .build()
        .addIf(
            channelService.canMakeChannelAdmin(user, channel),
            () ->
                linkTo(
                        methodOn(ChannelMemberApi.class)
                            .makeMemberAdmin(channel.getId(), user.getId()))
                    .withRel("makeAdmin"))
        .addIf(
            channelService.canRemoveGuest(user, channel),
            () ->
                linkTo(methodOn(ChannelMemberApi.class).removeMember(channel.getId(), user.getId()))
                    .withRel("removeMember"));
  }
}
