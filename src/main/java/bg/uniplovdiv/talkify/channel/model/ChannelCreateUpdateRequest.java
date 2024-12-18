package bg.uniplovdiv.talkify.channel.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChannelCreateUpdateRequest(@NotBlank @Size(min = 3, max = 255) String name) {}
