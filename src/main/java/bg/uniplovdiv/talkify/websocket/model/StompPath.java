package bg.uniplovdiv.talkify.websocket.model;

import static bg.uniplovdiv.talkify.utils.EncodedIdUtil.encode;
import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public enum StompPath {
  CHANNEL_PATH("/topic/chat/%s");

  String path;

  public String getPath(Long channelId) {
    return String.format(path, encode(channelId));
  }

  public String getPathAsMatcher() {
    return this.path.replace("%s", "*");
  }
}
