package bg.uniplovdiv.talkify.channel.service;

import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.channel.model.ChannelCreateUpdateRequest;
import bg.uniplovdiv.talkify.channel.model.ChannelSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChannelService {

  Channel create(ChannelCreateUpdateRequest request);

  Channel getById(Long id);

  Page<Channel> getChannelsByCriteria(ChannelSearchCriteria criteria, Pageable page);

  Channel update(Long id, ChannelCreateUpdateRequest request);

  void delete(Long id);
}
