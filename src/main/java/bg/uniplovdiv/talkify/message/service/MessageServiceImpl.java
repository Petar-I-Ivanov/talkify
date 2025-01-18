package bg.uniplovdiv.talkify.message.service;

import static bg.uniplovdiv.talkify.utils.SecurityUtils.isPermitted;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.throwIfNotAllowed;
import static bg.uniplovdiv.talkify.utils.constants.ChannelPermissions.SEND_MESSAGE;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.service.UserService;
import bg.uniplovdiv.talkify.channel.service.ChannelService;
import bg.uniplovdiv.talkify.message.model.Message;
import bg.uniplovdiv.talkify.message.model.MessageCreateUpdateRequest;
import bg.uniplovdiv.talkify.message.model.MessageRepository;
import bg.uniplovdiv.talkify.message.model.MessageSearchCriteria;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageServiceImpl implements MessageService {

  MessageRepository messageRepository;
  UserService userService;
  ChannelService channelService;

  @Override
  public Message create(MessageCreateUpdateRequest request) {
    throwIfNotAllowed(isPermitted(request.channelId(), SEND_MESSAGE));
    var message =
        Message.builder()
            .text(request.text())
            .sender(userService.getCurrentUser())
            .sentAt(LocalDateTime.now())
            .channel(channelService.getById(request.channelId()))
            .build();
    message.setActive(true);
    return messageRepository.save(message);
  }

  @Override
  public Message getById(Long id) {
    return messageRepository.getReferenceById(id);
  }

  @Override
  public Page<Message> getByCriteria(MessageSearchCriteria criteria, Pageable page) {
    if (criteria.channelId() != null) {
      throwIfNotAllowed(isPermitted(criteria.channelId(), SEND_MESSAGE));
    }

    return messageRepository.findAllByChannelId(criteria.channelId(), page);
  }

  @Override
  public boolean canUpdate(Message message) {
    return message.isCurrentUserSender();
  }

  @Override
  public Message update(Long id, MessageCreateUpdateRequest request) {
    var message = getById(id);
    throwIfNotAllowed(canUpdate(message));
    message.setText(request.text());
    message.setEditedAt(LocalDateTime.now());
    return messageRepository.save(message);
  }

  @Override
  public boolean canDelete(Message message) {
    return message.isCurrentUserSender();
  }

  @Override
  public void delete(Long id) {
    var message = getById(id);
    throwIfNotAllowed(canDelete(message));
    message.setActive(false);
    messageRepository.save(message);
  }
}
