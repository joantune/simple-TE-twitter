package twitter.simplified.clone.web;

import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import twitter.simplified.clone.domain.Tweet;

@RequestMapping("/tweets")
@Controller
@RooWebScaffold(path = "tweets", formBackingObject = Tweet.class)
public class TweetController {
}
