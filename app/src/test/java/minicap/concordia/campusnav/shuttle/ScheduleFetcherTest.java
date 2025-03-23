package minicap.concordia.campusnav.shuttle;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import minicap.concordia.campusnav.helpers.ScheduleFetcher;


public class ScheduleFetcherTest {

    @Test
    public void testFetchSchedule_Success() {
        // Mock the listener
        ScheduleFetcher.ScheduleFetchListener listener = mock(ScheduleFetcher.ScheduleFetchListener.class);

        // Call the fetch method
        ScheduleFetcher.fetch(listener);

        // Verify that the listener was called with the correct data
        verify(listener, timeout(5000)).onScheduleFetched(anyList());
    }
}
