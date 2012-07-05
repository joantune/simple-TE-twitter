package twitter.simplified.clone.domain;

import java.util.Date;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Follow {

    public Follow(User userFollowing, User userToBeFollowed) {
    	setFollowed(userToBeFollowed);
    	setFollower(userFollowing);
    	setCreationDate(new Date());
    	this.persist();
	}

	@NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date creationDate;

    @OneToOne
    @NotNull
    private User followed;

    @OneToOne
    @NotNull
    private User follower;
}
