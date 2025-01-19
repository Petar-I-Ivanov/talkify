package bg.uniplovdiv.talkify.auth.user.model;

import static bg.uniplovdiv.talkify.utils.QueryUtils.matchStart;
import static bg.uniplovdiv.talkify.utils.SecurityUtils.fetchUserId;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.utils.QueryUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;

@NoArgsConstructor(access = PRIVATE)
public class UserPredicates {

  private static QUser user = QUser.user;

  public static final Predicate buildPredicates(UserSearchCriteria criteria) {
    var predicate = new BooleanBuilder();

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::search)
            .map(
                search ->
                    user.email
                        .likeIgnoreCase(matchStart(search))
                        .or(user.username.likeIgnoreCase(matchStart(search))))
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::username)
            .map(QueryUtils::matchStart)
            .map(user.username::like)
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::email)
            .map(QueryUtils::matchStart)
            .map(user.email::like)
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::inChannelId)
            .map(
                channelId ->
                    user.ownedChannels
                        .any()
                        .id
                        .eq(channelId)
                        .or(user.adminChannels.any().id.eq(channelId))
                        .or(user.guestChannels.any().id.eq(channelId)))
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::notInChannelId)
            .map(
                notInChannelId ->
                    user.ownedChannels
                        .any()
                        .id
                        .eq(notInChannelId)
                        .not()
                        .and(user.adminChannels.any().id.eq(notInChannelId).not())
                        .and(user.guestChannels.any().id.eq(notInChannelId).not()))
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::active)
            .map(user.active::eq)
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::onlyFriends)
            .filter(BooleanUtils::isTrue)
            .map(o -> fetchUserId())
            .map(user.friendships.any().user.id::eq)
            .map(user.friendships.any().active::and)
            .map(predicate::and)
            .orElse(predicate);

    return predicate;
  }
}
