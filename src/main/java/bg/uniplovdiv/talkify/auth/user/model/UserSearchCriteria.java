package bg.uniplovdiv.talkify.auth.user.model;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;

public record UserSearchCriteria(
    String search,
    String username,
    String email,
    @EncodedId Long inChannelId,
    @EncodedId Long notInChannelId,
    Boolean onlyFriends,
    Boolean active) {}
