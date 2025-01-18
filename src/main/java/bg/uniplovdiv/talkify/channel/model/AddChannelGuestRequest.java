package bg.uniplovdiv.talkify.channel.model;

import jakarta.validation.constraints.NotNull;

public record AddChannelGuestRequest(@NotNull Long userId) {}
