package bg.uniplovdiv.talkify.security.annotations.permissions.channel;

import static bg.uniplovdiv.talkify.utils.constants.Permissions.CHANNEL_SELECT;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import bg.uniplovdiv.talkify.utils.SecurityUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize(SecurityUtils.SPEL_REFERENCE + ".isPermitted('" + CHANNEL_SELECT + "')")
public @interface ChannelSelect {}
