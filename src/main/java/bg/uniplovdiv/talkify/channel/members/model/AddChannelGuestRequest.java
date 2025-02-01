package bg.uniplovdiv.talkify.channel.members.model;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import jakarta.validation.constraints.NotNull;

public record AddChannelGuestRequest(@NotNull @EncodedId Long userId) {}
