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
import twitter.simplified.clone.domain.Follow;
import twitter.simplified.clone.domain.FollowDataOnDemand;
import twitter.simplified.clone.domain.FollowIntegrationTest;

privileged aspect FollowIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FollowIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FollowIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml");
    
    declare @type: FollowIntegrationTest: @Transactional;
    
    @Autowired
    private FollowDataOnDemand FollowIntegrationTest.dod;
    
    @Test
    public void FollowIntegrationTest.testCountFollows() {
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", dod.getRandomFollow());
        long count = Follow.countFollows();
        Assert.assertTrue("Counter for 'Follow' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FollowIntegrationTest.testFindFollow() {
        Follow obj = dod.getRandomFollow();
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Follow' failed to provide an identifier", id);
        obj = Follow.findFollow(id);
        Assert.assertNotNull("Find method for 'Follow' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Follow' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FollowIntegrationTest.testFindAllFollows() {
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", dod.getRandomFollow());
        long count = Follow.countFollows();
        Assert.assertTrue("Too expensive to perform a find all test for 'Follow', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Follow> result = Follow.findAllFollows();
        Assert.assertNotNull("Find all method for 'Follow' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Follow' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void FollowIntegrationTest.testFindFollowEntries() {
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", dod.getRandomFollow());
        long count = Follow.countFollows();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Follow> result = Follow.findFollowEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Follow' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Follow' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FollowIntegrationTest.testFlush() {
        Follow obj = dod.getRandomFollow();
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Follow' failed to provide an identifier", id);
        obj = Follow.findFollow(id);
        Assert.assertNotNull("Find method for 'Follow' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFollow(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Follow' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FollowIntegrationTest.testMergeUpdate() {
        Follow obj = dod.getRandomFollow();
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Follow' failed to provide an identifier", id);
        obj = Follow.findFollow(id);
        boolean modified =  dod.modifyFollow(obj);
        Integer currentVersion = obj.getVersion();
        Follow merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Follow' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FollowIntegrationTest.testPersist() {
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", dod.getRandomFollow());
        Follow obj = dod.getNewTransientFollow(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Follow' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Follow' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        Assert.assertNotNull("Expected 'Follow' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FollowIntegrationTest.testRemove() {
        Follow obj = dod.getRandomFollow();
        Assert.assertNotNull("Data on demand for 'Follow' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Follow' failed to provide an identifier", id);
        obj = Follow.findFollow(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Follow' with identifier '" + id + "'", Follow.findFollow(id));
    }
    
}
