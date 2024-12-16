package bg.uniplovdiv.talkify.auth.user.model;

public record UserSearchCriteria(
    String search, String username, String email, Boolean onlyFriends, Boolean active) {}
