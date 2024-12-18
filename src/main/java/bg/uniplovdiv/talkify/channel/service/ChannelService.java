package bg.uniplovdiv.talkify.channel.service;

import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.model.ChannelCreateUpdateRequest;
import bg.uniplovdiv.talkify.channel.model.ChannelSearchCriteria;
import bg.uniplovdiv.talkify.channel.model.UniqueNameRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChannelService {

  boolean canCreate();

  Channel create(ChannelCreateUpdateRequest request);

  boolean isNameExists(UniqueNameRequest request);

  Channel getById(Long id);

  Page<Channel> getAllByCriteria(ChannelSearchCriteria criteria, Pageable page);

  boolean canUpdate(Channel channel);

  Channel update(Long id, ChannelCreateUpdateRequest request);

  boolean canDelete(Channel channel);

  void delete(Long id);
}
