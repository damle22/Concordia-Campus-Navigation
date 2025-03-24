/*package minicap.concordia.campusnav.shuttle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import minicap.concordia.campusnav.components.ShuttleSchedule;
import minicap.concordia.campusnav.helpers.ShuttleScraper;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jsoup.class, ShuttleScraper.class})
public class ShuttleScraperTest {

    @Test
    public void testFetchSchedule_MondayThursday() throws IOException {
        // Mock the HTML content for Monday-Thursday
        String html = "<table><tr><td>9:15</td><td>9:30</td></tr><tr><td>9:30</td><td>9:45</td></tr></table>" +
                "<table><tr><td>9:15</td><td>9:45</td></tr><tr><td>9:30</td><td>10:00</td></tr></table>";
        Document doc = Jsoup.parse(html);

        // Mock Jsoup.connect to return the mock HTML
        PowerMockito.mockStatic(Jsoup.class);
        when(Jsoup.connect("https://www.concordia.ca/maps/shuttle-bus.html#depart")).thenReturn(mock(Connection.class));
        when(Jsoup.connect("https://www.concordia.ca/maps/shuttle-bus.html#depart").get()).thenReturn(doc);

        // Call the method
        List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

        // Verify the results
        assertEquals(4, schedules.size()); // 4 schedules (Loyola and SGW for Monday-Thursday and Friday)

        // Verify Monday-Thursday Loyola departures
        assertEquals("Monday-Thursday", schedules.get(0).getDay());
        assertEquals("Loyola", schedules.get(0).getCampus());
        assertEquals("9:15", schedules.get(0).getDepartureTimes().get(0));
        assertEquals("9:30", schedules.get(0).getDepartureTimes().get(1));

        // Verify Monday-Thursday SGW departures
        assertEquals("Monday-Thursday", schedules.get(1).getDay());
        assertEquals("SGW", schedules.get(1).getCampus());
        assertEquals("9:30", schedules.get(1).getDepartureTimes().get(0));
        assertEquals("9:45", schedules.get(1).getDepartureTimes().get(1));

        // Verify Friday Loyola departures
        assertEquals("Friday", schedules.get(2).getDay());
        assertEquals("Loyola", schedules.get(2).getCampus());
        assertEquals("9:15", schedules.get(2).getDepartureTimes().get(0));
        assertEquals("9:30", schedules.get(2).getDepartureTimes().get(1));

        // Verify Friday SGW departures
        assertEquals("Friday", schedules.get(3).getDay());
        assertEquals("SGW", schedules.get(3).getCampus());
        assertEquals("9:45", schedules.get(3).getDepartureTimes().get(0));
        assertEquals("10:00", schedules.get(3).getDepartureTimes().get(1));
    }
}*/