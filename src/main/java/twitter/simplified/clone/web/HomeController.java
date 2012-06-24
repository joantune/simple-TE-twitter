package twitter.simplified.clone.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/home")
@Controller
public class HomeController {
	
	@RequestMapping(value="index", produces="text/html")
	public String index()
	{
		return "home/index";
	}
	@RequestMapping(value="secured/home", produces="text/html")
	public String index(HttpServletRequest request)
	{
//		if (!request.isUserInRole("ROLE_ADMIN"))
//		{
//			 throw new AccessDeniedException("denied");
//		}
		return "home/secured/home";
	}
}
