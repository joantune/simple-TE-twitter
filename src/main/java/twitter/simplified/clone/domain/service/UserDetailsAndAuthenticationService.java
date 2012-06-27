/**
 * 
 */
package twitter.simplified.clone.domain.service;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.NoResultException;

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
public class UserDetailsAndAuthenticationService implements UserDetailsService {
	
	public class OwnUserDetailImplementation extends org.springframework.security.core.userdetails.User {
		
		
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
