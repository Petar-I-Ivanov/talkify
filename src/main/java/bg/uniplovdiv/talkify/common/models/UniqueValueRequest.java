package bg.uniplovdiv.talkify.common.models;

import jakarta.validation.constraints.NotBlank;

public record UniqueValueRequest(@NotBlank String value, Long exceptId) {}
