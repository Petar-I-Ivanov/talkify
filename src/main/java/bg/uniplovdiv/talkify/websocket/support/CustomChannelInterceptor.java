package bg.uniplovdiv.talkify.websocket.support;

import static bg.uniplovdiv.talkify.utils.SecurityUtils.isPermitted;
import static bg.uniplovdiv.talkify.utils.constants.ChannelPermissions.SEND_MESSAGE;
import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.CHANNEL_NOT_PERMITTED_EXC;
import static bg.uniplovdiv.talkify.utils.constants.LocalizedMessages.INVALID_CHANNEL_EXC;
import static bg.uniplovdiv.talkify.utils.constants.Regex.VAL_AFTER_LAST_SLASH_PTRN;
import static org.springframework.messaging.simp.stomp.StompCommand.SUBSCRIBE;

import bg.uniplovdiv.talkify.common.models.CustomAccessDenyException;
import bg.uniplovdiv.talkify.utils.EncodedIdUtil;
import java.util.Optional;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class CustomChannelInterceptor implements ChannelInterceptor {

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    var accessor = StompHeaderAccessor.wrap(message);
    if (SUBSCRIBE.equals(accessor.getCommand())) {

      var channelId = getChannelId(accessor);
      if (!isPermitted(channelId, SEND_MESSAGE)) {
        throw new CustomAccessDenyException(CHANNEL_NOT_PERMITTED_EXC);
      }
    }

    return message;
  }

  private Long getChannelId(StompHeaderAccessor accessor) {
    return Optional.ofNullable(accessor)
        .map(StompHeaderAccessor::getDestination)
        .filter(destination -> destination.matches(VAL_AFTER_LAST_SLASH_PTRN))
        .map(destination -> destination.replaceAll(VAL_AFTER_LAST_SLASH_PTRN, "$1"))
        .map(EncodedIdUtil::decoded)
        .orElseThrow(() -> new CustomAccessDenyException(INVALID_CHANNEL_EXC));
  }
}
