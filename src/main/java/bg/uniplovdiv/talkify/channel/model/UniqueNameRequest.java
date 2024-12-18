package bg.uniplovdiv.talkify.channel.model;

import jakarta.validation.constraints.NotBlank;

public record UniqueNameRequest(@NotBlank String name, Long exceptId) {}
