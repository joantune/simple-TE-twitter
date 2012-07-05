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

import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import twitter.simplified.clone.domain.User;

@RequestMapping("/users")
@Controller
@RooWebScaffold(path = "users", formBackingObject = User.class)
@RooWebJson(jsonObject = User.class)
public class UserController {

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid User user, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, user);
            return "redirect:/index?registerErrors=t";
        }
        uiModel.asMap().clear();
        try {
            user.persist();
        } catch (PersistenceException ex) {
            return "redirect:/index?registerErrors=t";
        }
        try {
            return "redirect:/index?pleaseLogin=t&fullName=" + UriUtils.encodePathSegment(user.getFullName(), WebUtils.DEFAULT_CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "redirect:/index?pleaseLogin=t";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> searchUser() {
    	 HttpHeaders headers = new HttpHeaders();
         headers.add("Content-Type", "application/json; charset=utf-8");
    	List<User> findAllUsers = User.findAllUsers();
        return new ResponseEntity<String>(User.toJsonArray(findAllUsers), headers, HttpStatus.OK);
    }
}
