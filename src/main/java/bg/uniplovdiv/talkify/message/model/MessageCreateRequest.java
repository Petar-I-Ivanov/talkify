package bg.uniplovdiv.talkify.message.model;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageCreateRequest(@NotBlank String text, @NotNull @EncodedId Long channelId) {}
