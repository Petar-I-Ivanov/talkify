package bg.uniplovdiv.talkify.message.model;

import jakarta.validation.constraints.NotNull;

public record MessageSearchCriteria(@NotNull Long channelId) {}
