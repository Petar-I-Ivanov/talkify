package bg.uniplovdiv.talkify.message.model;

import jakarta.validation.constraints.NotBlank;

public record MessageCreateUpdateRequest(@NotBlank String text, Long channelId) {}
