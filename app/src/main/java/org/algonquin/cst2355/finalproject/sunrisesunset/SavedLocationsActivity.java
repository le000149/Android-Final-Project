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


import com.google.android.material.snackbar.Snackbar;

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDAO;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/**
 * Activity for displaying saved locations in a RecyclerView.
 * Allows users to interact with each location item for further options like loading sunrise and sunset times
 * or deleting the location from the database.
 */
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
    /**
     * Loads saved locations from the database and displays them in the RecyclerView.
     */
    private void loadSavedLocations() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Location> locations = lDAO.getAllLocations();
            runOnUiThread(() -> recyclerView.setAdapter(new SavedLocationAdapter(locations)));
        });
    }
    /**
     * RecyclerView adapter for displaying saved location items.
     */
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
            holder.locationTextView.setText(getString(R.string.latitude)+location.getLatitude()+"   " +getString(R.string.longitude)+ location.getLongitude());
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }
        /**
         * ViewHolder for location items within the RecyclerView.
         * Provides UI and interaction logic for each saved location item.
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView locationTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                locationTextView = itemView.findViewById(R.id.textViewLocation);
                itemView.setOnClickListener(clk -> {
                    int position = getAdapterPosition();
                    String message = getString(R.string.question_sun) + " " + locationTextView.getText();
                    AlertDialog.Builder builder = new AlertDialog.Builder(SavedLocationsActivity.this);
                    builder.setMessage(message);
                    builder.setTitle("Question");
                    builder.setPositiveButton("Load", (dialog, cl) -> {
                                if (position != RecyclerView.NO_POSITION) {
                                    Location location = locations.get(position);
                                    SunriseSunsetResultActivity.launch(SavedLocationsActivity.this,location.getLatitude(), location.getLongitude());
                                }
                            })
                            .setNegativeButton("Delete", (dialog, cl) -> {
                                if (position != RecyclerView.NO_POSITION) {
                                    Location location = locations.get(position);
                                    Executor executor = Executors.newSingleThreadExecutor();
                                    executor.execute(() -> {
                                        lDAO.deleteLocation(location.getLatitude(), location.getLongitude());
                                        runOnUiThread(() -> {
                                            locations.remove(position);
                                            notifyItemRemoved(position);
                                            Snackbar.make(recyclerView, "Location deleted", Snackbar.LENGTH_LONG).setAction("Undo",click->{
                                                locations.add(position,location);
                                                notifyItemInserted(position);
                                            }).show();
                                        });
                                    });
                                }
                            }).create().show();
                });
            }
        }

            }
        }





