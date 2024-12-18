package bg.uniplovdiv.talkify.security.annotations.roles;

import static bg.uniplovdiv.talkify.auth.role.model.RoleNames.CHANNEL_ADMIN;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize("@accessService.hasRole('" + CHANNEL_ADMIN + "')")
public @interface ChannelAdminRole {}
