package bg.uniplovdiv.talkify.channel.members.model;

import jakarta.validation.constraints.NotNull;

public record AddChannelGuestRequest(@NotNull Long userId) {}
