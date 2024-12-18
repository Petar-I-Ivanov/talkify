package bg.uniplovdiv.talkify.channel.service;

import static bg.uniplovdiv.talkify.channel.model.ChannelPredicates.buildPredicates;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.model.ChannelCreateUpdateRequest;
import bg.uniplovdiv.talkify.channel.model.ChannelRepository;
import bg.uniplovdiv.talkify.channel.model.ChannelSearchCriteria;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// add can create/edit/delete methods and validate
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ChannelServiceImpl implements ChannelService {

  ChannelRepository channelRepository;
  UserService userService;

  // add unique name validation
  @Override
  public Channel create(ChannelCreateUpdateRequest request) {
    var channel = new Channel();
    channel.setName(request.name());
    channel.setActive(true);
    channel.setOwner(userService.getCurrentUser());
    return channelRepository.save(channel);
  }

  @Override
  public Channel getById(Long id) {
    return channelRepository.getReferenceById(id);
  }

  @Override
  public Page<Channel> getChannelsByCriteria(ChannelSearchCriteria criteria, Pageable page) {
    return channelRepository.findAll(buildPredicates(criteria), page);
  }

  // add unique name validation
  @Override
  public Channel update(Long id, ChannelCreateUpdateRequest request) {
    var channel = getById(id);
    channel.setName(request.name());
    return channelRepository.save(channel);
  }

  @Override
  public void delete(Long id) {
    var channel = getById(id);
    channel.setActive(false);
    channelRepository.save(channel);
  }
}
