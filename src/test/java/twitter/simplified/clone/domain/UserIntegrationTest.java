package twitter.simplified.clone.domain;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.roo.addon.test.RooIntegrationTest;

@RooIntegrationTest(entity = User.class)
public class UserIntegrationTest {

	@Autowired
	private UserDataOnDemand dod;
	
	@Autowired
	private TweetDataOnDemand tweetDod;
	
    @Test
    public void testMarkerMethod() {
    }
    
    @Test
    public void testTweets()
    {
    	User userA = dod.getRandomUser();
    	Tweet randomTweet1 = tweetDod.getRandomTweet();
    	Set<Tweet> userATweets = new HashSet<Tweet>();
    	userATweets.add(randomTweet1);
    	userA.setOwnedTweets(userATweets);
    	
    	Assert.assertTrue("The owned tweets must be the same", userA.getOwnedTweets().containsAll(userATweets));
    	Assert.assertTrue("And it must have randomTweet1", userA.getOwnedTweets().contains(randomTweet1));
    }
    
    @Test
    public void testTweetGetter() {
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(2012, 02, 20, 10, 00);
    	Date oldestDate = calendar.getTime();
    	
    	calendar.add(Calendar.DAY_OF_MONTH, 1);
    	
    	Date dayMoreDate = calendar.getTime();
    	
    	Assert.assertTrue("day more date must be more recent", dayMoreDate.after(oldestDate));
    	
    	calendar.add(Calendar.DAY_OF_MONTH, 1);
    	Date twoDaysMoreDate = calendar.getTime();
    	calendar.add(Calendar.DAY_OF_MONTH, 1);
    	Date threeDaysMoreDate = calendar.getTime();
    	
    	User userA = dod.getRandomUser();
    	Tweet randomBeforeTweet = tweetDod.getRandomTweet();
    	randomBeforeTweet.setCreationDate(oldestDate);
    	User userB = dod.getRandomUser();
    	
    	Set<Tweet> aTweets = new HashSet<Tweet>();
    	aTweets.add(randomBeforeTweet);
    	
    	//simple case
    	
    	//make B follow A, A makes tweet, B makes newer tweet, B sees one of A's tweet and his own
    	
    	Follow follow = new Follow(userB, userA);
    	follow.setCreationDate(dayMoreDate);
    	userB.getFollows().add(follow);
    	
    	Tweet aTweet = tweetDod.getRandomTweet();
    	aTweet.setCreationDate(twoDaysMoreDate);
    	aTweets.add(aTweet);
    	userA.setOwnedTweets(aTweets);
    	
    	Tweet bTweet = tweetDod.getRandomTweet();
    	bTweet.setCreationDate(threeDaysMoreDate);
    	userB.getOwnedTweets().add(bTweet);
    	
    	Assert.assertTrue("number of own tweets wrong, userB: " + userB.getOwnedTweets().size() + " userA: " + userA.getOwnedTweets().size(),userB.getOwnedTweets().size() == 1 && userB.getOwnedTweets().size() == 2);
    	
    	
    	
    	
    	
    }
}
