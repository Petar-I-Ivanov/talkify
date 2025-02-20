package bg.uniplovdiv.talkify.security.api;

import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = PRIVATE)
public class CsrfApi {

  @Value("${server.servlet.session.cookie.same-site:None}")
  String sameSitePolicy;

  @GetMapping("/csrf-token")
  public CsrfToken csrfToken(CsrfToken csrf, HttpServletResponse response) {
    response.addHeader(SET_COOKIE, buildCsrfCookie(csrf));
    return csrf;
  }

  private String buildCsrfCookie(CsrfToken csrf) {
    return ResponseCookie.from(csrf.getHeaderName(), csrf.getToken())
        .httpOnly(false)
        .secure(true)
        .sameSite(sameSitePolicy)
        .build()
        .toString();
  }
}
