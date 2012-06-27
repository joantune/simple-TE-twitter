package twitter.simplified.clone.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import twitter.simplified.clone.domain.service.UserDetailsAndAuthenticationService.OwnUserDetailImplementation;


@RequestMapping("/home")
@Controller
public class HomeController {
	
	@RequestMapping(value="index", produces="text/html")
	public String index()
	{
		return "home/index";
	}
	@RequestMapping(value="secured/home", produces="text/html")
	public String index(HttpServletRequest request, Authentication auth, Model uiModel)
	{
		@SuppressWarnings("unused")
		OwnUserDetailImplementation ownUserDetailImplementation = (OwnUserDetailImplementation) auth.getPrincipal();
		uiModel.addAttribute("user", ownUserDetailImplementation.getUser());
//		if (!request.isUserInRole("ROLE_ADMIN"))
//		{
//			 throw new AccessDeniedException("denied");
//		}
		return "home/secured/home";
	}
}
