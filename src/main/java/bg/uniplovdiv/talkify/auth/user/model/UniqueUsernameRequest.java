package bg.uniplovdiv.talkify.auth.user.model;

import jakarta.validation.constraints.NotBlank;

public record UniqueUsernameRequest(@NotBlank String username, Long exceptId) {}
