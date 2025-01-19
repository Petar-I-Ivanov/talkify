package bg.uniplovdiv.talkify.channel.service;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.channel.members.model.AddChannelGuestRequest;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.model.ChannelCreateUpdateRequest;
import bg.uniplovdiv.talkify.channel.model.ChannelSearchCriteria;
import bg.uniplovdiv.talkify.common.models.UniqueValueRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChannelService {

  boolean canCreate();

  Channel create(ChannelCreateUpdateRequest request);

  Channel createPrivate(User user, User friend);

  boolean isNameExists(UniqueValueRequest request);

  Channel getById(Long id);

  Page<Channel> getAllByCriteria(ChannelSearchCriteria criteria, Pageable page);

  boolean canAddMember(Channel channel);

  void addMember(Long channelId, AddChannelGuestRequest request);

  boolean canRemoveGuest(User user, Channel channel);

  void removeGuest(Long channelId, Long guestId);

  boolean canMakeChannelAdmin(User user, Channel channel);

  void makeChannelAdmin(Long channelId, Long guestId);

  boolean canUpdate(Channel channel);

  Channel update(Long id, ChannelCreateUpdateRequest request);

  boolean canDelete(Channel channel);

  void delete(Long id);
}
