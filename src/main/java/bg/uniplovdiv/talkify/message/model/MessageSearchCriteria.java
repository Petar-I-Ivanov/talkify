package bg.uniplovdiv.talkify.message.model;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import jakarta.validation.constraints.NotNull;

public record MessageSearchCriteria(@NotNull @EncodedId Long channelId) {}
