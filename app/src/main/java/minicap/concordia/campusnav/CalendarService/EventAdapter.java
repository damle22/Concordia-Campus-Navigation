package minicap.concordia.campusnav.CalendarService;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import minicap.concordia.campusnav.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<EventItem> eventList;

    public EventAdapter(List<EventItem> eventList) {
        this.eventList = eventList;
    }

    public void setData(List<EventItem> newData) {
        this.eventList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventItem item = eventList.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

        Date startDate = new Date(item.getStartTime().getValue());
        Date endDate = new Date(item.getEndTime().getValue());

        String formattedDate = dateFormat.format(startDate);
        String formattedTimeRange = timeFormat.format(startDate) + " – " + timeFormat.format(endDate);

        holder.titleText.setText("📚 " + item.getTitle());
        holder.subtitleText.setText("📅 " + formattedDate + "   🕒 " + formattedTimeRange + "\n📍 " + item.getLocation());

        // Default background
        holder.itemView.setBackgroundColor(0xFFFFFFFF);

        long now = System.currentTimeMillis();

        // Ongoing class (now)
        if (now >= startDate.getTime() && now <= endDate.getTime()) {
            holder.itemView.setBackgroundColor(0xFFDFF0D8); // green
        }
        // Next upcoming class (first class in the future)
        else if (isNextUpcomingEvent(position, now)) {
            holder.itemView.setBackgroundColor(0xFFFFE5E5); // light red
        }

        // Add divider styling
        holder.itemView.setBackgroundResource(R.drawable.item_divider);
    }


    private boolean isNextUpcomingEvent(int position, long now) {
        for (int i = 0; i < eventList.size(); i++) {
            Date futureStart = new Date(eventList.get(i).getStartTime().getValue());
            if (futureStart.getTime() > now) {
                return i == position;
            }
        }
        return false;
    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, subtitleText;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(android.R.id.text1);
            subtitleText = itemView.findViewById(android.R.id.text2);
        }
    }
}
