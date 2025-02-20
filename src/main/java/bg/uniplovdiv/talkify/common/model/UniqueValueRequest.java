package bg.uniplovdiv.talkify.common.model;

import bg.uniplovdiv.talkify.common.encodedid.EncodedId;
import jakarta.validation.constraints.NotBlank;

public record UniqueValueRequest(@NotBlank String value, @EncodedId Long exceptId) {}
