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
import twitter.simplified.clone.domain.Follow;
import twitter.simplified.clone.domain.FollowDataOnDemand;
import twitter.simplified.clone.domain.User;
import twitter.simplified.clone.domain.UserDataOnDemand;

privileged aspect FollowDataOnDemand_Roo_DataOnDemand {
    
    declare @type: FollowDataOnDemand: @Component;
    
    private Random FollowDataOnDemand.rnd = new SecureRandom();
    
    private List<Follow> FollowDataOnDemand.data;
    
    @Autowired
    private UserDataOnDemand FollowDataOnDemand.userDataOnDemand;
    
    public Follow FollowDataOnDemand.getNewTransientFollow(int index) {
        Follow obj = new Follow();
        setCreationDate(obj, index);
        setFollowed(obj, index);
        setFollower(obj, index);
        return obj;
    }
    
    public void FollowDataOnDemand.setCreationDate(Follow obj, int index) {
        Date creationDate = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setCreationDate(creationDate);
    }
    
    public void FollowDataOnDemand.setFollowed(Follow obj, int index) {
        User followed = userDataOnDemand.getSpecificUser(index);
        obj.setFollowed(followed);
    }
    
    public void FollowDataOnDemand.setFollower(Follow obj, int index) {
        User follower = userDataOnDemand.getSpecificUser(index);
        obj.setFollower(follower);
    }
    
    public Follow FollowDataOnDemand.getSpecificFollow(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Follow obj = data.get(index);
        Long id = obj.getId();
        return Follow.findFollow(id);
    }
    
    public Follow FollowDataOnDemand.getRandomFollow() {
        init();
        Follow obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Follow.findFollow(id);
    }
    
    public boolean FollowDataOnDemand.modifyFollow(Follow obj) {
        return false;
    }
    
    public void FollowDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Follow.findFollowEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Follow' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Follow>();
        for (int i = 0; i < 10; i++) {
            Follow obj = getNewTransientFollow(i);
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