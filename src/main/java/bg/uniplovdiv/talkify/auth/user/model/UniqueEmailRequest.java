package bg.uniplovdiv.talkify.auth.user.model;

public record UniqueEmailRequest(String email, Long exceptId) {}
