package bg.uniplovdiv.talkify.channel.service;

import static bg.uniplovdiv.talkify.channel.model.ChannelPredicates.buildPredicates;
import static bg.uniplovdiv.talkify.common.models.DataValidationException.throwIfCondition;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.isPermitted;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.revalidateUser;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.throwIfNotAllowed;
import static bg.uniplovdiv.talkify.utils.constants.ChannelPermissions.ADD_GUEST;
import static bg.uniplovdiv.talkify.utils.constants.ChannelPermissions.CHANGE_NAME;
import static bg.uniplovdiv.talkify.utils.constants.ChannelPermissions.DELETE_CHANNEL;
import static bg.uniplovdiv.talkify.utils.constants.ChannelPermissions.MAKE_ADMIN;
import static bg.uniplovdiv.talkify.utils.constants.ChannelPermissions.REMOVE_GUEST;
import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.NAME_TAKEN_EXC;
import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.USER_ALREADY_IN_CHANNEL_EXC;
import static bg.uniplovdiv.talkify.utils.constants.Permissions.CHANNEL_CREATE;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.role.service.RoleService;
import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.channel.members.model.AddChannelGuestRequest;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.model.ChannelCreateUpdateRequest;
import bg.uniplovdiv.talkify.channel.model.ChannelRepository;
import bg.uniplovdiv.talkify.channel.model.ChannelSearchCriteria;
import bg.uniplovdiv.talkify.common.models.UniqueValueRequest;
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
  RoleService roleService;

  @Override
  public boolean canCreate() {
    return isPermitted(CHANNEL_CREATE);
  }

  @Override
  public Channel create(ChannelCreateUpdateRequest request) {
    throwIfNotAllowed(canCreate());
    throwIfCondition(isNameExists(new UniqueValueRequest(request.name(), null)), NAME_TAKEN_EXC);

    var owner = userService.getCurrentUser();
    owner.getRoles().add(roleService.getChannelOwenrRole());

    var channel = new Channel();
    channel.setName(request.name());
    channel.setActive(true);
    channel.setPrivate(false);
    channel.setOwner(owner);
    channel = channelRepository.save(channel);

    owner.getOwnedChannels().add(channel);
    revalidateUser(owner);
    return channel;
  }

  @Override
  public Channel createPrivate(User user, User friend) {
    var channel = new Channel();
    channel.setName(String.format("%s - %s", user.getUsername(), friend.getUsername()));
    channel.setActive(true);
    channel.setPrivate(true);
    channel.setOwner(user);
    channel.getAdmins().add(friend);
    channel = channelRepository.save(channel);
    revalidateUser(user);
    return channel;
  }

  @Override
  public boolean isNameExists(UniqueValueRequest request) {
    return Optional.ofNullable(request)
        .map(UniqueValueRequest::exceptId)
        .map(exceptId -> channelRepository.existsByNameAndIdNot(request.value(), exceptId))
        .orElseGet(() -> channelRepository.existsByName(request.value()));
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
  public boolean canAddMember(Channel channel) {
    return !channel.isPrivate() && isPermitted(channel.getId(), ADD_GUEST);
  }

  @Override
  public void addMember(Long channelId, AddChannelGuestRequest request) {
    var channel = getById(channelId);
    throwIfNotAllowed(canAddMember(channel));
    throwIfCondition(channel.isUserAlreadyInChannel(request.userId()), USER_ALREADY_IN_CHANNEL_EXC);
    var user = userService.getById(request.userId());
    channel.getGuests().add(user);
    channelRepository.save(channel);
    userService.addGuestRole(user);
  }

  @Override
  public boolean canRemoveGuest(User user, Channel channel) {
    return !channel.isPrivate()
        && isPermitted(channel.getId(), REMOVE_GUEST)
        && !channel.getOwner().getId().equals(user.getId())
        && channel.isUserAlreadyInChannel(user.getId());
  }

  @Override
  public void removeGuest(Long channelId, Long guestId) {
    var channel = getById(channelId);
    var guest = userService.getById(guestId);
    throwIfNotAllowed(canRemoveGuest(guest, channel));

    var removedAdminList =
        channel.getAdmins().stream().filter(user -> !user.getId().equals(guestId)).collect(toSet());
    var removedGuestList =
        channel.getGuests().stream().filter(user -> !user.getId().equals(guestId)).collect(toSet());

    channel.setAdmins(removedAdminList);
    channel.setGuests(removedGuestList);
    channelRepository.save(channel);
  }

  @Override
  public boolean canMakeChannelAdmin(User user, Channel channel) {
    return !channel.isPrivate()
        && isPermitted(channel.getId(), MAKE_ADMIN)
        && channel.getGuests().stream().map(User::getId).anyMatch(user.getId()::equals);
  }

  @Override
  public void makeChannelAdmin(Long channelId, Long guestId) {
    var channel = getById(channelId);
    var guest = userService.getById(guestId);
    throwIfNotAllowed(canMakeChannelAdmin(guest, channel));

    var removedGuestList =
        channel.getGuests().stream().filter(user -> !user.getId().equals(guestId)).collect(toSet());
    channel.setGuests(removedGuestList);
    channel.getAdmins().add(guest);
    channelRepository.save(channel);
    userService.addAdminRole(guest);
  }

  @Override
  public boolean canUpdate(Channel channel) {
    return !channel.isPrivate() && isPermitted(channel.getId(), CHANGE_NAME);
  }

  @Override
  public Channel update(Long id, ChannelCreateUpdateRequest request) {
    var channel = getById(id);
    throwIfNotAllowed(canUpdate(channel));
    throwIfCondition(isNameExists(new UniqueValueRequest(request.name(), id)), NAME_TAKEN_EXC);
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
