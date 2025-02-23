package minicap.concordia.campusnav;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import minicap.concordia.campusnav.beans.Campus;
import minicap.concordia.campusnav.firebase.PersistableHelper;

public class PersistableHelperUnitTest {

    @Test
    public void testFetchAll_Campus() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1); // Ensures the test waits for async calls

        Set<String> validCampusIds = new HashSet<>();
        PersistableHelper.fetchAll(Campus.class, campuses -> {
            for (Campus campus : campuses) {
                validCampusIds.add(campus.getId());
            }
            latch.countDown(); // Release the test after async fetch
        });

        latch.await();

        assertTrue("Expected campuses were not fetched", validCampusIds.size() == 2 && validCampusIds.contains("SGW") && validCampusIds.contains("LYL"));
    }

    //TODO Add testFetchAll_Buildings once all buildings are added to db
}
