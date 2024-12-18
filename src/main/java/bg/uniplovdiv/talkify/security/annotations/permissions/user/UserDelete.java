package bg.uniplovdiv.talkify.security.annotations.permissions.user;

import static bg.uniplovdiv.talkify.auth.permission.model.PermissionValues.USER_DELETE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

@Target(METHOD)
@Retention(RUNTIME)
@PreAuthorize("@accessService.hasPermission('" + USER_DELETE + "')")
public @interface UserDelete {}
