package bg.uniplovdiv.talkify.message.api;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.common.PagedRepresentationAssembler;
import bg.uniplovdiv.talkify.message.model.Message;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageModelAssembler extends PagedRepresentationAssembler<Message, MessageModel> {

  public MessageModelAssembler() {
    super(Message.class, MessageModel.class);
  }

  @Override
  public MessageModel toModel(Message message) {
    return MessageModel.builder()
        .id(message.getId())
        .text(message.isActive() ? message.getText() : null)
        .sentAt(message.getSentAt())
        .editedAt(message.getEditedAt())
        .isCurrentUserSender(message.isCurrentUserSender())
        .build();
  }
}
