package bg.uniplovdiv.talkify.security;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfApi {

  @GetMapping("/csrf-token")
  public CsrfToken csrfToken(CsrfToken token, HttpServletResponse response) {
    response.addHeader(SET_COOKIE, token.getHeaderName() + "=" + token.getToken());
    return token;
  }
}
