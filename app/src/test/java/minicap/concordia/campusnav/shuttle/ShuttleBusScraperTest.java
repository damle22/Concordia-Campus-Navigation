package minicap.concordia.campusnav.shuttle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import minicap.concordia.campusnav.components.ShuttleSchedule;
import minicap.concordia.campusnav.helpers.ShuttleScraper;

public class ShuttleBusScraperTest {

    @Test
    public void testFetchSchedule_MondayThursday() {
        // Mock HTML content for Monday-Thursday
        String html = "<table><tr><td>9:15</td><td>9:30</td></tr><tr><td>9:30</td><td>9:45</td></tr></table>";
        Document doc = Jsoup.parse(html);

        // Call the method
        List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

        // Verify the results
        assertEquals(2, schedules.size()); // 2 schedules (Loyola and SGW)
        assertEquals("Monday-Thursday", schedules.get(0).getDay());
        assertEquals("Loyola", schedules.get(0).getCampus());
        assertEquals("9:15", schedules.get(0).getDepartureTimes().get(0));
        assertEquals("9:30", schedules.get(0).getDepartureTimes().get(1));

        assertEquals("Monday-Thursday", schedules.get(1).getDay());
        assertEquals("SGW", schedules.get(1).getCampus());
        assertEquals("9:30", schedules.get(1).getDepartureTimes().get(0));
        assertEquals("9:45", schedules.get(1).getDepartureTimes().get(1));
    }

    @Test
    public void testFetchSchedule_Friday() {
        // Mock HTML content for Friday
        String html = "<table><tr><td>9:15</td><td>9:45</td></tr><tr><td>9:30</td><td>10:00</td></tr></table>";
        Document doc = Jsoup.parse(html);

        // Call the method
        List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

        // Verify the results
        assertEquals(2, schedules.size()); // 2 schedules (Loyola and SGW)
        assertEquals("Friday", schedules.get(0).getDay());
        assertEquals("Loyola", schedules.get(0).getCampus());
        assertEquals("9:15", schedules.get(0).getDepartureTimes().get(0));
        assertEquals("9:30", schedules.get(0).getDepartureTimes().get(1));

        assertEquals("Friday", schedules.get(1).getDay());
        assertEquals("SGW", schedules.get(1).getCampus());
        assertEquals("9:45", schedules.get(1).getDepartureTimes().get(0));
        assertEquals("10:00", schedules.get(1).getDepartureTimes().get(1));
    }
}