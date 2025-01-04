package bg.uniplovdiv.talkify.message.service;

import bg.uniplovdiv.talkify.message.model.Message;
import bg.uniplovdiv.talkify.message.model.MessageCreateUpdateRequest;
import bg.uniplovdiv.talkify.message.model.MessageSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  Message create(MessageCreateUpdateRequest request);

  Message getById(Long id);

  Page<Message> getByCriteria(MessageSearchCriteria criteria, Pageable page);

  boolean canUpdate(Message message);

  Message update(Long id, MessageCreateUpdateRequest request);

  boolean canDelete(Message message);

  void delete(Long id);
}
