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
package twitter.simplified.clone.domain;

import java.io.ObjectInputStream.GetField;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import javax.persistence.Access;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NoResultException;
import javax.persistence.Transient;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import flexjson.JSONSerializer;
import twitter.simplified.clone.utils.RandomString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findUsersByUsernameLikeOrEmailAddressLikeOrFullNameLike", "findUsersByUsernameEquals" })
@RooJson
public class User implements UserDetailsService {

    @Transient
    private static ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);

    @Transient
    private static RandomString randomSaltGenerator = new RandomString(9);

    @NotNull
    @Column(unique = true)
    @Size(min = 4, max = 16)
    private String username;

    @NotNull
    @Column(unique = true)
    @Size(min = 4, max = 254)
    private String emailAddress;

    @NotNull
    @Size(min = 4, max = 100)
    private String fullName;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tweet> ownedTweets = new HashSet<Tweet>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="user_follows", joinColumns=@JoinColumn(name="followedId"), inverseJoinColumns=@JoinColumn(name="followerId"))
    private Set<twitter.simplified.clone.domain.User> followers = new HashSet<twitter.simplified.clone.domain.User>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="user_follows", joinColumns=@JoinColumn(name="followerId") , inverseJoinColumns=@JoinColumn(name="followedId"))
    private Set<twitter.simplified.clone.domain.User> followed = new HashSet<twitter.simplified.clone.domain.User>();

    @NotNull
    @Size(min = 64, max = 64)
    private String password;

    @NotNull
    @Size(min = 9, max = 9)
    private String randomSalt;

    public String getTempPasswordContainer() {
        return "";
    }

    public int getNumberFollowed() {
        return getFollowed().size();
    }

    public int getNumberFollowers() {
        Hibernate.initialize(getFollowers());
        return getFollowers().size();
    }

    public int getNumberOwnTweets() {
        Hibernate.initialize(getOwnedTweets());
        return getOwnedTweets().size();
    }

    public void setTempPasswordContainer(String tempPasswordContainer) {
        Assert.hasLength(tempPasswordContainer);
        Assert.state(tempPasswordContainer.length() > 6 && tempPasswordContainer.length() < 32, "Password must be bigger than 6 and less than 32 characters");
        String salt = randomSaltGenerator.nextString();
        setRandomSalt(salt);
        setPassword(passwordEncoder.encodePassword(tempPasswordContainer, salt));
    }
    
    /**
     * 
     * @param collection
     * @return a json array without the useless details
     */
    public static String toJsonArrayWithoutDetails(Collection<User> collection)
    {
    	return new JSONSerializer().exclude("*.class", "password", "randomSalt", "emailAddress", "numberFollowed", "numberFollowers", "numberOwnTweets", "tempPasswordContainer", "version").serialize(collection);
    }
    
    public static String toJsonArray(Collection<User> collection)
    {
    	return new JSONSerializer().exclude("*.class", "password", "randomSalt").serialize(collection);
    }
    
    public String toJson()
    {
    	return new JSONSerializer().exclude("*.class", "password", "randomSalt").serialize(this);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User singleResult = null;
        try {
            singleResult = User.findUsersByUsernameEquals(username).getSingleResult();
        } catch (NoResultException e) {
            throw new UsernameNotFoundException("no username found by TypedQuery.getSingleResult", e);
        }
        if (singleResult == null) throw new DataAccessException("error retrieving a User") {
        };
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, singleResult.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        return user;
    }
}
