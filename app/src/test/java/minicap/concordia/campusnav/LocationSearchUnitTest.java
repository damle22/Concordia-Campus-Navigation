package minicap.concordia.campusnav;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import java.util.List;

import minicap.concordia.campusnav.helpers.LocationSearchHelper;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class LocationSearchUnitTest {

    @Test
    public void testValidSearch() throws Exception {
        // Mock API response
        String mockResponse = "{ \"predictions\": [" +
                "{ \"description\": \"New York, USA\" }, " +
                "{ \"description\": \"New York City\" }" +
                "] }";

        List<String> results = LocationSearchHelper.parseLocationResults(mockResponse);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(2, results.size());
        assertEquals("New York, USA", results.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptySearchQuery() throws Exception {
        LocationSearchHelper.searchLocation("");
    }
}
