package bg.uniplovdiv.talkify.security.annotations.permissions.user;

import static bg.uniplovdiv.talkify.utils.constants.Permissions.USER_SEARCH;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize("@accessService.hasAllPermissions('" + USER_SEARCH + "')")
public @interface UserSearch {}
