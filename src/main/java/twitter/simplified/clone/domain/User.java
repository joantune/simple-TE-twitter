package twitter.simplified.clone.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class User {

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
    private Set<twitter.simplified.clone.domain.User> followers = new HashSet<twitter.simplified.clone.domain.User>();

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<twitter.simplified.clone.domain.User> followed = new HashSet<twitter.simplified.clone.domain.User>();
}
