package bg.uniplovdiv.talkify.channel.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import bg.uniplovdiv.talkify.channel.model.ChannelCreateUpdateRequest;
import bg.uniplovdiv.talkify.channel.model.ChannelSearchCriteria;
import bg.uniplovdiv.talkify.channel.service.ChannelService;
import bg.uniplovdiv.talkify.common.models.UniqueValueRequest;
import bg.uniplovdiv.talkify.security.annotations.permissions.channel.ChannelCreate;
import bg.uniplovdiv.talkify.security.annotations.permissions.channel.ChannelCreateOrUpdate;
import bg.uniplovdiv.talkify.security.annotations.permissions.channel.ChannelDelete;
import bg.uniplovdiv.talkify.security.annotations.permissions.channel.ChannelSearch;
import bg.uniplovdiv.talkify.security.annotations.permissions.channel.ChannelSelect;
import bg.uniplovdiv.talkify.security.annotations.permissions.channel.ChannelUpdate;
import jakarta.validation.Valid;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChannelApi {

  ChannelService channelService;
  ChannelModelAssembler channelModelAssembler;

  @ChannelCreate
  @PostMapping
  @ResponseStatus(CREATED)
  public ChannelModel create(@Valid @RequestBody ChannelCreateUpdateRequest request) {
    return channelModelAssembler.toModel(channelService.create(request));
  }

  @ChannelCreateOrUpdate
  @GetMapping("/exists/name")
  @ResponseStatus(OK)
  public boolean existsByName(UniqueValueRequest request) {
    return channelService.isNameExists(request);
  }

  @ChannelSelect
  @GetMapping("/{id}/members")
  @ResponseStatus(OK)
  public Collection<ChannelMemberModel> getChannelMembers(@PathVariable Long id) {
    return channelModelAssembler.toChannelMembers(channelService.getById(id));
  }

  @ChannelSelect
  @GetMapping("/{id}")
  @ResponseStatus(OK)
  public ChannelModel getById(@PathVariable Long id) {
    return channelModelAssembler.toModel(channelService.getById(id));
  }

  @ChannelSearch
  @GetMapping
  @ResponseStatus(OK)
  public PagedModel<ChannelModel> getAllByCriteria(ChannelSearchCriteria criteria, Pageable page) {
    return channelModelAssembler.toPagedModel(channelService.getAllByCriteria(criteria, page));
  }

  @ChannelUpdate
  @PutMapping("/{id}")
  @ResponseStatus(OK)
  public ChannelModel update(
      @PathVariable Long id, @Valid @RequestBody ChannelCreateUpdateRequest request) {
    return channelModelAssembler.toModel(channelService.update(id, request));
  }

  @ChannelDelete
  @DeleteMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    channelService.delete(id);
    return ResponseEntity.status(NO_CONTENT.value()).build();
  }
}
