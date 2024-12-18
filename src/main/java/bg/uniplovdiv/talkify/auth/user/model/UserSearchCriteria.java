package bg.uniplovdiv.talkify.auth.user.model;

public record UserSearchCriteria(
    String search,
    String username,
    String email,
    Long inChannelId,
    Boolean onlyFriends,
    Boolean active) {}
