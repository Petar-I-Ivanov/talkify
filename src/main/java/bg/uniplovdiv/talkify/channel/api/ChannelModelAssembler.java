package bg.uniplovdiv.talkify.channel.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import bg.uniplovdiv.talkify.channel.members.api.ChannelMemberApi;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.service.ChannelService;
import bg.uniplovdiv.talkify.common.PagedRepresentationAssembler;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChannelModelAssembler extends PagedRepresentationAssembler<Channel, ChannelModel> {

  ChannelService channelService;

  public ChannelModelAssembler(ChannelService channelService) {
    super(ChannelApi.class, ChannelModel.class);
    this.channelService = channelService;
  }

  @Override
  public ChannelModel toModel(Channel channel) {
    return ChannelModel.builder()
        .id(channel.getId())
        .name(channel.getName())
        .build()
        .addIf(
            channelService.canAddMember(channel),
            () ->
                linkTo(methodOn(ChannelMemberApi.class).addMember(channel.getId(), null))
                    .withRel("addMember"))
        .addIf(
            channelService.canUpdate(channel),
            () ->
                linkTo(methodOn(ChannelApi.class).update(channel.getId(), null)).withRel("update"))
        .addIf(
            channelService.canDelete(channel),
            () -> linkTo(methodOn(ChannelApi.class).delete(channel.getId())).withRel("delete"));
  }
}
