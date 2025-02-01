package bg.uniplovdiv.talkify.channel.model;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;

public record ChannelSearchCriteria(
    String name,
    @EncodedId Long userId,
    @EncodedId Long ownerId,
    @EncodedId Long adminId,
    @EncodedId Long guestId,
    Boolean active) {}
