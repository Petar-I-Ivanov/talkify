package bg.uniplovdiv.talkify.auth.user.model;

import static bg.uniplovdiv.talkify.utils.constants.Regex.PASSWORD_PTRN;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotBlank @Size(min = 3, max = 64) String username,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8) @Pattern(regexp = PASSWORD_PTRN) String password,
    @NotBlank String confirmPassword) {}
