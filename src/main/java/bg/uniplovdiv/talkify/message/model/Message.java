package bg.uniplovdiv.talkify.message.model;

import static bg.uniplovdiv.talkify.utils.SecurityUtils.fetchUserId;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.common.models.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Message extends BaseEntity {

  @Column(nullable = false, columnDefinition = "TEXT")
  String text;

  @Column(nullable = false)
  Long senderId;

  @Column(nullable = false)
  LocalDateTime sentAt;

  LocalDateTime editedAt;

  @ManyToOne(fetch = LAZY, optional = false)
  @JoinColumn(name = "channel_id", nullable = false)
  Channel channel;

  public boolean isCurrentUserSender() {
    return senderId.equals(fetchUserId());
  }
}
