package twitter.simplified.clone.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;
import org.springframework.roo.addon.web.mvc.controller.json.RooWebJson;
import org.springframework.security.core.context.SecurityContextHolder;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
@RooJson
@RooWebJson(jsonObject = User.class)
public class Tweet {

    @NotNull
    @Size(min = 5, max = 140)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date creationDate;

    @NotNull
    @ManyToOne
    private User ownerUser;

    public static String toJsonArray(Collection<twitter.simplified.clone.domain.Tweet> collection) {
        return new JSONSerializer().exclude("*.class", "ownerUser.emailAddress", "ownerUser.numberFollowed", "ownerUser.numberFollowers", "ownerUser.numberOwnTweets", "ownerUser.password", "ownerUser.randomSalt", "ownerUser.tempPasswordContainer", "ownerUser.version").serialize(collection);
    }

    public static twitter.simplified.clone.domain.Tweet fromJsonContentToTweet(String json, User owner) {
        Tweet tweet = new JSONDeserializer<Tweet>().use(null, Tweet.class).deserialize(json);
        tweet.setCreationDate(new Date());
        tweet.setOwnerUser(owner);
        return tweet;
    }

    public String toJson() {
        return new JSONSerializer().exclude("*.class", "ownerUser.emailAddress", "ownerUser.numberFollowed", "ownerUser.numberFollowers", "ownerUser.numberOwnTweets", "ownerUser.password", "ownerUser.randomSalt", "ownerUser.tempPasswordContainer", "ownerUser.version").serialize(this);
    }
}
