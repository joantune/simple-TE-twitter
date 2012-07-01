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
// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package twitter.simplified.clone.domain;

import flexjson.JSONDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import twitter.simplified.clone.domain.User;

privileged aspect User_Roo_Json {
    
    public static User User.fromJsonToUser(String json) {
        return new JSONDeserializer<User>().use(null, User.class).deserialize(json);
    }
    
    public static Collection<User> User.fromJsonArrayToUsers(String json) {
        return new JSONDeserializer<List<User>>().use(null, ArrayList.class).use("values", User.class).deserialize(json);
    }
    
}
