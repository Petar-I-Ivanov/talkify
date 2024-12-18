package bg.uniplovdiv.talkify.channel.model;

public record ChannelSearchCriteria(
    String name, Long userId, Long ownerId, Long adminId, Long guestId, Boolean active) {}
