package minicap.concordia.campusnav.components;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import minicap.concordia.campusnav.R;
// Change the import to use the correct Building class:
import minicap.concordia.campusnav.buildingmanager.entities.Building;

public class BuildingAdapter extends RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder> {

    private List<Building> buildingList;

    public BuildingAdapter(List<Building> buildingList) {
        this.buildingList = buildingList;
    }

    @NonNull
    @Override
    public BuildingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each building item (ensure this matches your layout file)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_building, parent, false);
        return new BuildingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuildingViewHolder holder, int position) {
        Building building = buildingList.get(position);
        // Use the proper getter for the building name – note that in Building.java it is getBuildingName()
        holder.tvBuildingName.setText(building.getBuildingName());
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
            tvBuildingName = itemView.findViewById(R.id.tvBuildingName);
        }
    }
}
