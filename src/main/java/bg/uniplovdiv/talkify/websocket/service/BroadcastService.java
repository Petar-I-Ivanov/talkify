package bg.uniplovdiv.talkify.websocket.service;

import bg.uniplovdiv.talkify.message.api.MessageModel;

public interface BroadcastService {

  void notifyAboutNewMessage(Long channelId, MessageModel message);
}
