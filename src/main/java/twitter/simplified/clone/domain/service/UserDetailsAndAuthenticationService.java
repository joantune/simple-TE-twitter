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
/**
 * 
 */
package twitter.simplified.clone.domain.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import javax.persistence.NoResultException;
import javax.persistence.Transient;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import twitter.simplified.clone.domain.User;

/**
 * @author joantune
 * 
 * Makes the bridge between the domain and the {@link DaoAuthenticationProvider}
 */
public class UserDetailsAndAuthenticationService implements UserDetailsService, Serializable {
	
	/**
	 * First version :)
	 */
	private static final long serialVersionUID = 1L;

	public class OwnUserDetailImplementation extends org.springframework.security.core.userdetails.User {
		
		
		@Transient
		private final User user;
		
		public OwnUserDetailImplementation(User user, String username, String password,
				Collection<? extends GrantedAuthority> authorities) {
			this(user, username, password, true, true, true,
					true, authorities);
		}
		public OwnUserDetailImplementation(User user, String username, String password,
				boolean enabled, boolean accountNonExpired,
				boolean credentialsNonExpired, boolean accountNonLocked,
				Collection<? extends GrantedAuthority> authorities) {
			super(username, password, enabled, accountNonExpired, credentialsNonExpired,
					accountNonLocked, authorities);
			Assert.notNull(user, "user must not be null");
			this.user = user;
		}
		
		public String getSalt()
		{
			return user.getRandomSalt();
		}

		public User getUser() {
			return user;
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User singleResult = null;
		try {
			singleResult = User.findUsersByUsernameEquals(username)
					.getSingleResult();
		} catch (NoResultException e) {
			throw new UsernameNotFoundException(
					"no username found by TypedQuery.getSingleResult", e);
		}

		if (singleResult == null)
			throw new DataAccessException("error retrieving a User") {
			};

		OwnUserDetailImplementation user = new OwnUserDetailImplementation(singleResult,
				username, singleResult.getPassword(),
				Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
		return user;
	}

}
