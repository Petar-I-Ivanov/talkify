package bg.uniplovdiv.talkify.channel.service;

import static bg.uniplovdiv.talkify.auth.permission.model.ChannelPermissions.CHANGE_NAME;
import static bg.uniplovdiv.talkify.auth.permission.model.ChannelPermissions.DELETE_CHANNEL;
import static bg.uniplovdiv.talkify.auth.permission.model.PermissionValues.CHANNEL_CREATE;
import static bg.uniplovdiv.talkify.channel.model.ChannelPredicates.buildPredicates;
import static bg.uniplovdiv.talkify.common.exeptions.DataValidationException.throwIfCondition;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.isPermitted;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.throwIfNotAllowed;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.model.ChannelCreateUpdateRequest;
import bg.uniplovdiv.talkify.channel.model.ChannelRepository;
import bg.uniplovdiv.talkify.channel.model.ChannelSearchCriteria;
import bg.uniplovdiv.talkify.channel.model.UniqueNameRequest;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChannelServiceImpl implements ChannelService {

  ChannelRepository channelRepository;
  UserService userService;

  @Override
  public boolean canCreate() {
    return isPermitted(CHANNEL_CREATE);
  }

  @Override
  public Channel create(ChannelCreateUpdateRequest request) {
    throwIfNotAllowed(canCreate());
    throwIfCondition(isNameExists(new UniqueNameRequest(request.name(), null)), "Name is taken.");

    var channel = new Channel();
    channel.setName(request.name());
    channel.setActive(true);
    channel.setOwner(userService.getCurrentUser());
    return channelRepository.save(channel);
  }

  @Override
  public boolean isNameExists(UniqueNameRequest request) {
    return Optional.ofNullable(request)
        .map(UniqueNameRequest::exceptId)
        .map(exceptId -> channelRepository.existsByNameAndIdNot(request.name(), exceptId))
        .orElseGet(() -> channelRepository.existsByName(request.name()));
  }

  @Override
  public Channel getById(Long id) {
    return channelRepository.getReferenceById(id);
  }

  @Override
  public Page<Channel> getAllByCriteria(ChannelSearchCriteria criteria, Pageable page) {
    return channelRepository.findAll(buildPredicates(criteria), page);
  }

  @Override
  public boolean canUpdate(Channel channel) {
    return isPermitted(channel.getId(), CHANGE_NAME);
  }

  @Override
  public Channel update(Long id, ChannelCreateUpdateRequest request) {
    var channel = getById(id);
    throwIfNotAllowed(canUpdate(channel));

    throwIfCondition(isNameExists(new UniqueNameRequest(request.name(), id)), "Name is taken.");

    channel.setName(request.name());
    return channelRepository.save(channel);
  }

  @Override
  public boolean canDelete(Channel channel) {
    return isPermitted(channel.getId(), DELETE_CHANNEL);
  }

  @Override
  public void delete(Long id) {
    var channel = getById(id);
    throwIfNotAllowed(canDelete(channel));

    channel.setActive(false);
    channelRepository.save(channel);
  }
}
