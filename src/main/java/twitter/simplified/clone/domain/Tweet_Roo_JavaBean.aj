// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package twitter.simplified.clone.domain;

import java.util.Date;
import twitter.simplified.clone.domain.Tweet;
import twitter.simplified.clone.domain.User;

privileged aspect Tweet_Roo_JavaBean {
    
    public String Tweet.getContent() {
        return this.content;
    }
    
    public void Tweet.setContent(String content) {
        this.content = content;
    }
    
    public Date Tweet.getCreationDate() {
        return this.creationDate;
    }
    
    public void Tweet.setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public User Tweet.getOwnerUser() {
        return this.ownerUser;
    }
    
    public void Tweet.setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }
    
}
