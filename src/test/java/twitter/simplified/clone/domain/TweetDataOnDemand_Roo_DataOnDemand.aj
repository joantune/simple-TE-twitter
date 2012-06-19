// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package twitter.simplified.clone.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twitter.simplified.clone.domain.Tweet;
import twitter.simplified.clone.domain.TweetDataOnDemand;
import twitter.simplified.clone.domain.User;
import twitter.simplified.clone.domain.UserDataOnDemand;

privileged aspect TweetDataOnDemand_Roo_DataOnDemand {
    
    declare @type: TweetDataOnDemand: @Component;
    
    private Random TweetDataOnDemand.rnd = new SecureRandom();
    
    private List<Tweet> TweetDataOnDemand.data;
    
    @Autowired
    private UserDataOnDemand TweetDataOnDemand.userDataOnDemand;
    
    public Tweet TweetDataOnDemand.getNewTransientTweet(int index) {
        Tweet obj = new Tweet();
        setContent(obj, index);
        setCreationDate(obj, index);
        setOwnerUser(obj, index);
        return obj;
    }
    
    public void TweetDataOnDemand.setContent(Tweet obj, int index) {
        String content = "content_" + index;
        if (content.length() > 140) {
            content = content.substring(0, 140);
        }
        obj.setContent(content);
    }
    
    public void TweetDataOnDemand.setCreationDate(Tweet obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void TweetDataOnDemand.setOwnerUser(Tweet obj, int index) {
        User ownerUser = userDataOnDemand.getRandomUser();
        obj.setOwnerUser(ownerUser);
    }
    
    public Tweet TweetDataOnDemand.getSpecificTweet(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Tweet obj = data.get(index);
        Long id = obj.getId();
        return Tweet.findTweet(id);
    }
    
    public Tweet TweetDataOnDemand.getRandomTweet() {
        init();
        Tweet obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Tweet.findTweet(id);
    }
    
    public boolean TweetDataOnDemand.modifyTweet(Tweet obj) {
        return false;
    }
    
    public void TweetDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Tweet.findTweetEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Tweet' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Tweet>();
        for (int i = 0; i < 10; i++) {
            Tweet obj = getNewTransientTweet(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}