/*******************************************************************************
 * Copyright (C) 2012 João Antunes
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package twitter.simplified.clone.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Hibernate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import twitter.simplified.clone.domain.Tweet;
import twitter.simplified.clone.domain.User;
import twitter.simplified.clone.domain.UserDataOnDemand;
import twitter.simplified.clone.domain.service.UserDetailsAndAuthenticationService.OwnUserDetailImplementation;


@RequestMapping("/home")
@Controller
public class HomeController {
	
	@RequestMapping(value="index", produces="text/html")
	public String index()
	{
		return "home/index";
	}
	@Transactional
	@RequestMapping(value="secured/home", produces="text/html")
	public String index(HttpServletRequest request, Authentication auth, Model uiModel)
	{
		@SuppressWarnings("unused")
		OwnUserDetailImplementation ownUserDetailImplementation = (OwnUserDetailImplementation) auth.getPrincipal();
		User user = ownUserDetailImplementation.getUser();
		user = user.findUser(user.getId());
		uiModel.addAttribute("user", user);
		uiModel.addAttribute("numberFollowed", user.getNumberFollowed());
		uiModel.addAttribute("numberFollowers", user.getNumberFollowers());
		uiModel.addAttribute("numberOwnTweets", user.getNumberOwnTweets());
		uiModel.addAttribute("listUsers", User.toJsonArrayWithoutDetails((User.findAllUsers())));
		uiModel.addAttribute("listTweets", Tweet.toJsonArray(Tweet.findAllTweets()));
//		if (!request.isUserInRole("ROLE_ADMIN"))
//		{
//			 throw new AccessDeniedException("denied");
//		}
		return "home/secured/home";
	}
	
	 @RequestMapping(value="secured/search/{searchString}", method=RequestMethod.GET, produces="application/json")
	 public @ResponseBody ResponseEntity<String> searchUser(@PathVariable("searchString") String searchString) {
    	 HttpHeaders headers = new HttpHeaders();
         headers.add("Content-Type", "application/json; charset=utf-8");
		 List<User> users = User.findUsersByUsernameLikeOrEmailAddressLikeOrFullNameLike(searchString, searchString, searchString).getResultList();
		 return  new ResponseEntity<String>(User.toJsonArrayWithoutDetails(users),headers,HttpStatus.OK);
		 
	 }
	
	
}
