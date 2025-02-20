package bg.uniplovdiv.talkify.websocket.service;

import static bg.uniplovdiv.talkify.websocket.model.StompPath.CHANNEL_PATH;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.message.api.MessageModel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class BroadcastServiceImpl implements BroadcastService {

  SimpMessagingTemplate stompBroadcast;

  @Override
  public void notifyAboutNewMessage(Long channelId, MessageModel message) {
    stompBroadcast.convertAndSend(CHANNEL_PATH.getPath(channelId), message);
  }
}
