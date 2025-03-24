/*package minicap.concordia.campusnav.shuttle;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import minicap.concordia.campusnav.components.ShuttleSchedule;
import minicap.concordia.campusnav.helpers.ScheduleFetcher;

public class ScheduleFetcherTest {

    @Test
    public void testFetchSchedule_Success() throws InterruptedException {
        // Create a CountDownLatch to wait for the callback
        CountDownLatch latch = new CountDownLatch(1);

        // Mock the listener
        ScheduleFetcher.ScheduleFetchListener listener = mock(ScheduleFetcher.ScheduleFetchListener.class);

        // Mock the ShuttleScraper.fetchSchedule() method
        List<ShuttleSchedule> dummySchedules = Collections.singletonList(
                new ShuttleSchedule("Monday-Thursday", "Loyola", Collections.singletonList("9:15"))
        );

        // Call the fetch method
        ScheduleFetcher.fetch(listener);

        // Wait for the callback to be invoked (timeout after 5 seconds)
        assertTrue(latch.await(5, TimeUnit.SECONDS));

        // Verify that the listener was called with the correct data
        verify(listener).onScheduleFetched(dummySchedules);
    }
}*/
