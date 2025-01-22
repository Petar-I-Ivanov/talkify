package bg.uniplovdiv.talkify.message.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.hateoas.server.core.DummyInvocationUtils.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import bg.uniplovdiv.talkify.common.PagedRepresentationAssembler;
import bg.uniplovdiv.talkify.message.model.Message;
import bg.uniplovdiv.talkify.message.service.MessageService;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageModelAssembler extends PagedRepresentationAssembler<Message, MessageModel> {

  MessageService messageService;

  public MessageModelAssembler(MessageService messageService) {
    super(MessageApi.class, MessageModel.class);
    this.messageService = messageService;
  }

  @Override
  public MessageModel toModel(Message message) {
    return MessageModel.builder()
        .id(message.getId())
        .text(message.isActive() ? message.getText() : null)
        .sender(message.getSender().getUsername())
        .sentAt(message.getSentAt())
        .editedAt(message.getEditedAt())
        .build()
        .addIf(
            messageService.canUpdate(message),
            () ->
                linkTo(methodOn(MessageApi.class).update(message.getId(), null)).withRel("update"))
        .addIf(
            messageService.canDelete(message),
            () -> linkTo(methodOn(MessageApi.class).delete(message.getId())).withRel("delete"));
  }
}
