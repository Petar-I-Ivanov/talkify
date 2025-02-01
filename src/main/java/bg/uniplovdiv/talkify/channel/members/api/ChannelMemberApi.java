package bg.uniplovdiv.talkify.channel.members.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.OK;

import bg.uniplovdiv.talkify.channel.members.model.AddChannelGuestRequest;
import bg.uniplovdiv.talkify.channel.service.ChannelService;
import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import bg.uniplovdiv.talkify.security.annotations.Authenticated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channels/{id}/members")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChannelMemberApi {

  ChannelService channelService;
  ChannelMemberModelAssembler channelMemberModelAssembler;

  @Authenticated
  @PostMapping
  @ResponseStatus(OK)
  public ResponseEntity<Void> addMember(
      @PathVariable @EncodedId Long id, @Valid @RequestBody AddChannelGuestRequest request) {
    channelService.addMember(id, request);
    return ResponseEntity.ok().build();
  }

  @Authenticated
  @GetMapping
  @ResponseStatus(OK)
  public CollectionModel<ChannelMemberModel> getChannelMembers(@PathVariable @EncodedId Long id) {
    return CollectionModel.of(
        channelMemberModelAssembler.toChannelMembers(channelService.getById(id)));
  }

  @Authenticated
  @PatchMapping("/{userId}/admin")
  @ResponseStatus(OK)
  public ResponseEntity<Void> makeMemberAdmin(
      @PathVariable @EncodedId Long id, @PathVariable @EncodedId Long userId) {
    channelService.makeChannelAdmin(id, userId);
    return ResponseEntity.ok().build();
  }

  @Authenticated
  @DeleteMapping("/{userId}")
  @ResponseStatus(OK)
  public ResponseEntity<Void> removeMember(
      @PathVariable @EncodedId Long id, @PathVariable @EncodedId Long userId) {
    channelService.removeGuest(id, userId);
    return ResponseEntity.ok().build();
  }
}
