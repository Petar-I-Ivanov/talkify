package bg.uniplovdiv.talkify.auth.user.model;

public record UserSearchCriteria(
    String search,
    String username,
    String email,
    Long inChannelId,
    Long notInChannelId,
    Boolean onlyFriends,
    Boolean active) {}
