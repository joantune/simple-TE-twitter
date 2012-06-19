package twitter.simplified.clone.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter.simplified.clone.domain.User;

@RequestMapping("/users")
@Controller
@RooWebScaffold(path = "users", formBackingObject = User.class)
public class UserController {
}
