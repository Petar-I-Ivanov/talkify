package bg.uniplovdiv.talkify.config;

import static bg.uniplovdiv.talkify.websocket.model.StompPath.CHANNEL_PATH;
import static org.springframework.messaging.simp.SimpMessageType.CONNECT;
import static org.springframework.messaging.simp.SimpMessageType.CONNECT_ACK;
import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.OTHER;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;
import static org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager.Builder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfiguration {

  @Bean
  public AuthorizationManager<Message<?>> messageAuthorizationManager(Builder messages) {
    return messages
        .nullDestMatcher()
        .authenticated()
        .simpSubscribeDestMatchers(CHANNEL_PATH.getPathAsMatcher())
        .authenticated()
        .simpDestMatchers("/app/**")
        .hasRole("USER")
        .simpTypeMatchers(CONNECT, CONNECT_ACK, SUBSCRIBE)
        .authenticated()
        .simpTypeMatchers(MESSAGE, OTHER)
        .denyAll()
        .build();
  }
}
