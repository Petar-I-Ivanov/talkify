package bg.uniplovdiv.talkify.security.annotations.permissions.channel;

import static bg.uniplovdiv.talkify.auth.permission.model.PermissionValues.CHANNEL_CREATE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize("@accessService.hasPermission('" + CHANNEL_CREATE + "')")
public @interface ChannelCreate {}