package bg.uniplovdiv.talkify.security.annotations.permissions.channel;

import static bg.uniplovdiv.talkify.utils.constants.Permissions.CHANNEL_CREATE;
import static bg.uniplovdiv.talkify.utils.constants.Permissions.CHANNEL_UPDATE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize(
    "@accessService.hasAnyPermissions({'" + CHANNEL_CREATE + "','" + CHANNEL_UPDATE + "})")
public @interface ChannelCreateOrUpdate {}
