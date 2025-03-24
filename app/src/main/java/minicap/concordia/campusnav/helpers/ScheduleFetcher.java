package minicap.concordia.campusnav.helpers;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

import minicap.concordia.campusnav.components.ShuttleSchedule;

public class ScheduleFetcher {

    public interface ScheduleFetchListener {
        void onScheduleFetched(List<ShuttleSchedule> schedules);
    }

    public static void fetch(ScheduleFetchListener listener) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Background work: Fetch the schedule
            List<ShuttleSchedule> schedules = ShuttleScraper.fetchSchedule();

            // Update the UI on the main thread
            handler.post(() -> listener.onScheduleFetched(schedules));
        });
    }
}