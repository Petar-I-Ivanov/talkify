package bg.uniplovdiv.talkify.security.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping({"/", "/sign-in", "/sign-up"})
  public String index() {
    return "forward:index.html";
  }
}
