package org.algonquin.cst2355.finalproject.sunrisesunset;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDAO;

import java.util.List;
import java.util.concurrent.Executors;

public class SavedLocationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LocationDAO lDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations); // Tạo layout này trong res/layout

        recyclerView = findViewById(R.id.recyclerViewSavedLocations); // Đảm bảo có RecyclerView này trong layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lDAO = MainApplication.getLocationDB().LocationDao();

        loadSavedLocations();
    }

    private void loadSavedLocations() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Location> locations = lDAO.getAllLocations();
            runOnUiThread(() -> recyclerView.setAdapter(new SavedLocationAdapter(locations)));
        });
    }

    public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.ViewHolder> {

        private final List<Location> locations;

        public SavedLocationAdapter(List<Location> locations) {
            this.locations = locations;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Location location = locations.get(position);
            holder.locationTextView.setText("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView locationTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                locationTextView = itemView.findViewById(R.id.textViewLocation);
                itemView.setOnClickListener(clk -> {
                    int position = getAdapterPosition();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SavedLocationsActivity.this);
                    builder.setMessage("Do you want to load this location again: " + locationTextView.getText());
                    builder.setTitle("Question");
                    builder.setPositiveButton("Yes", (dialog, cl) -> {
                                if (position != RecyclerView.NO_POSITION) {
                                    Location location = locations.get(position);

                                    SunriseSunsetResultActivity.launch(SavedLocationsActivity.this,location.getLatitude(), location.getLongitude());
                                }
                            })
                            .setNegativeButton("No", (dialog, cl) -> {

                            }).create().show();
                });
            }
        }
    }
}



