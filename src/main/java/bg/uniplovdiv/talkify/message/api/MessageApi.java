package bg.uniplovdiv.talkify.message.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

import bg.uniplovdiv.talkify.message.model.MessageCreateUpdateRequest;
import bg.uniplovdiv.talkify.message.model.MessageSearchCriteria;
import bg.uniplovdiv.talkify.message.service.MessageService;
import bg.uniplovdiv.talkify.security.annotations.Authenticated;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class MessageApi {

  MessageService messageService;
  MessageModelAssembler messageModelAssembler;

  @Authenticated
  @PostMapping
  @ResponseStatus(CREATED)
  public MessageModel create(@Valid @RequestBody MessageCreateUpdateRequest request) {
    return messageModelAssembler.toModel(messageService.create(request));
  }

  @Authenticated
  @GetMapping
  @ResponseStatus(OK)
  public PagedModel<MessageModel> getAllByCriteria(MessageSearchCriteria criteria, Pageable page) {
    return messageModelAssembler.toPagedModel(messageService.getByCriteria(criteria, page));
  }

  @Authenticated
  @PutMapping("/{id}")
  @ResponseStatus(OK)
  public MessageModel update(
      @PathVariable Long id, @Valid @RequestBody MessageCreateUpdateRequest request) {
    return messageModelAssembler.toModel(messageService.update(id, request));
  }

  @Authenticated
  @DeleteMapping("/{id}")
  @ResponseStatus(NO_CONTENT)
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    messageService.delete(id);
    return ResponseEntity.status(NO_CONTENT.value()).build();
  }
}
