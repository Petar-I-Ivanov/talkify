package bg.uniplovdiv.talkify.channel.model;

import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.utils.QueryUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ChannelPredicates {

  private static QChannel channel = QChannel.channel;

  public static final Predicate buildPredicates(ChannelSearchCriteria criteria) {
    var predicate = new BooleanBuilder();

    predicate =
        Optional.ofNullable(criteria)
            .map(ChannelSearchCriteria::name)
            .map(QueryUtils::matchStart)
            .map(channel.name::likeIgnoreCase)
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(ChannelSearchCriteria::userId)
            .map(
                userId ->
                    channel
                        .owner
                        .id
                        .eq(userId)
                        .or(channel.admins.any().id.eq(userId))
                        .or(channel.guests.any().id.eq(userId)))
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(ChannelSearchCriteria::ownerId)
            .map(channel.owner.id::eq)
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(ChannelSearchCriteria::adminId)
            .map(channel.admins.any().id::eq)
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(ChannelSearchCriteria::guestId)
            .map(channel.guests.any().id::eq)
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(ChannelSearchCriteria::active)
            .map(channel.active::eq)
            .map(predicate::and)
            .orElse(predicate);

    return predicate;
  }
}
