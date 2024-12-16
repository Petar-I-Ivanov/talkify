package bg.uniplovdiv.talkify.auth.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotBlank @Size(min = 3, max = 64) String username, @NotBlank @Email String email) {}
