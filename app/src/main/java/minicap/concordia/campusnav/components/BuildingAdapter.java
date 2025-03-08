package minicap.concordia.campusnav.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import minicap.concordia.campusnav.R;
import minicap.concordia.campusnav.beans.Building;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder> {

    private List<Building> buildingList;

    public BuildingAdapter(List<Building> buildingList) {
        this.buildingList = buildingList;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_building.xml layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new BuildingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        Building b = buildingList.get(position);
        // Set the building name on the TextView
        holder.tvBuildingName.setText(b.getName());
    }

    @Override
    public int getItemCount() {
        return buildingList.size();
    }

    public void updateBuildings(List<Building> newList) {
        this.buildingList = newList;
        notifyDataSetChanged();
    }

    static class BuildingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBuildingName;

        public BuildingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Make sure this ID matches the TextView in item_building.xml
            tvBuildingName = itemView.findViewById(R.id.tvBuildingName);
        }
    }
}
