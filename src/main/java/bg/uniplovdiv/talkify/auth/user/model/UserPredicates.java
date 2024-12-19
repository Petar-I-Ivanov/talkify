package bg.uniplovdiv.talkify.auth.user.model;

import static bg.uniplovdiv.talkify.utils.QueryUtils.matchStart;
import static lombok.AccessLevel.PRIVATE;

import bg.uniplovdiv.talkify.utils.QueryUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import lombok.NoArgsConstructor;

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
                    user.adminChannels
                        .any()
                        .id
                        .eq(channelId)
                        .or(user.adminChannels.any().id.eq(channelId))
                        .or(user.guestChannels.any().id.eq(channelId)))
            .map(predicate::and)
            .orElse(predicate);

    predicate =
        Optional.ofNullable(criteria)
            .map(UserSearchCriteria::active)
            .map(user.active::eq)
            .map(predicate::and)
            .orElse(predicate);

    return predicate;
  }
}
