package bg.uniplovdiv.talkify.message.model;

import jakarta.validation.constraints.NotBlank;

public record MessageUpdateRequest(@NotBlank String text) {}
