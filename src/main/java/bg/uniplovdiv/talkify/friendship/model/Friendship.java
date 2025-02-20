package bg.uniplovdiv.talkify.friendship.model;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.auth.user.model.User;
import bg.uniplovdiv.talkify.channel.model.Channel;
import bg.uniplovdiv.talkify.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class Friendship extends BaseEntity {

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "friend_id", nullable = false)
  User friend;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "channel_id", nullable = false)
  Channel channel;
}
