// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package twitter.simplified.clone.domain;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import twitter.simplified.clone.domain.Tweet;
import twitter.simplified.clone.domain.TweetDataOnDemand;
import twitter.simplified.clone.domain.TweetIntegrationTest;

privileged aspect TweetIntegrationTest_Roo_IntegrationTest {
    
    declare @type: TweetIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: TweetIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: TweetIntegrationTest: @Transactional;
    
    @Autowired
    private TweetDataOnDemand TweetIntegrationTest.dod;
    
    @Test
    public void TweetIntegrationTest.testCountTweets() {
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", dod.getRandomTweet());
        long count = Tweet.countTweets();
        Assert.assertTrue("Counter for 'Tweet' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void TweetIntegrationTest.testFindTweet() {
        Tweet obj = dod.getRandomTweet();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to provide an identifier", id);
        obj = Tweet.findTweet(id);
        Assert.assertNotNull("Find method for 'Tweet' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Tweet' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void TweetIntegrationTest.testFindAllTweets() {
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", dod.getRandomTweet());
        long count = Tweet.countTweets();
        Assert.assertTrue("Too expensive to perform a find all test for 'Tweet', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Tweet> result = Tweet.findAllTweets();
        Assert.assertNotNull("Find all method for 'Tweet' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Tweet' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void TweetIntegrationTest.testFindTweetEntries() {
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", dod.getRandomTweet());
        long count = Tweet.countTweets();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Tweet> result = Tweet.findTweetEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Tweet' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Tweet' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void TweetIntegrationTest.testFlush() {
        Tweet obj = dod.getRandomTweet();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to provide an identifier", id);
        obj = Tweet.findTweet(id);
        Assert.assertNotNull("Find method for 'Tweet' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyTweet(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Tweet' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void TweetIntegrationTest.testMergeUpdate() {
        Tweet obj = dod.getRandomTweet();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to provide an identifier", id);
        obj = Tweet.findTweet(id);
        boolean modified =  dod.modifyTweet(obj);
        Integer currentVersion = obj.getVersion();
        Tweet merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Tweet' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void TweetIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", dod.getRandomTweet());
        Tweet obj = dod.getNewTransientTweet(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Tweet' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Tweet' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Tweet' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void TweetIntegrationTest.testRemove() {
        Tweet obj = dod.getRandomTweet();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Tweet' failed to provide an identifier", id);
        obj = Tweet.findTweet(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Tweet' with identifier '" + id + "'", Tweet.findTweet(id));
    }
    
}
