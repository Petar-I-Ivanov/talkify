package bg.uniplovdiv.talkify.message.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageCreateUpdateRequest(@NotBlank String text, @NotNull Long channelId) {}
