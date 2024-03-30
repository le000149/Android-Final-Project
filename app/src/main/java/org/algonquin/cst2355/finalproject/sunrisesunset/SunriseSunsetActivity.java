package org.algonquin.cst2355.finalproject.sunrisesunset;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivitySunriseSunsetBinding;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDAO;

import java.util.List;
import java.util.concurrent.Executors;
/**
 * An activity that allows users to search for sunrise and sunset times based on latitude and longitude.
 * It also displays a list of previously searched locations, allowing users to select a location to view its
 * sunrise and sunset times. The activity saves the last searched location in SharedPreferences.
 */
public class SunriseSunsetActivity extends AppCompatActivity {
    private static final String SP_KEY_LAST_LATITUDE = "last_latitude";
    private static final String SP_KEY_LAST_LONGITUDE = "last_longitude";
    private static final String SP_NAME = "sunrise_sunset_prefs";
    private LocationDAO lDAO;

    private ActivitySunriseSunsetBinding binding;
    /**
     * Initializes the activity, its views, and sets up the interaction logic.
     * Loads the last searched location from SharedPreferences and displays it in the input fields.
     * Sets up a RecyclerView to display previously searched locations.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySunriseSunsetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarSun);
        lDAO = MainApplication.getLocationDB().LocationDao();
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        binding.editTextLatitude.setText(sharedPreferences.getString(SP_KEY_LAST_LATITUDE, ""));
        binding.editTextLongitude.setText(sharedPreferences.getString(SP_KEY_LAST_LONGITUDE, ""));

        binding.searchSunButton.setOnClickListener(v -> {
            String latitude = binding.editTextLatitude.getText().toString();
            String longitude = binding.editTextLongitude.getText().toString();
            if (latitude.isEmpty() || longitude.isEmpty()) {
                Toast.makeText(this,R.string.enter_location_to_search, Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferences.edit()
                        .putString(SP_KEY_LAST_LATITUDE, latitude)
                        .putString(SP_KEY_LAST_LONGITUDE, longitude)
                        .apply();

               SunriseSunsetResultActivity.launch(this,latitude,longitude);
            }
        });

        showSavedLocations();
    }
    /**
     * Displays previously searched locations by fetching distinct latitude and longitude values from the database.
     * This method asynchronously queries the database and updates the RecyclerView on the UI thread.
     */
    private void showSavedLocations() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<String> latitudes = lDAO.getAllLatitudesDistinct();
                List<String> longtiudes=lDAO.getAllLogitudesDistinct();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.recyclerSun.setAdapter(new SunriseSunsetActivity.LocationAdapter(latitudes,longtiudes));
                        binding.recyclerSun.setLayoutManager(new LinearLayoutManager(SunriseSunsetActivity.this));
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sunrise_sunset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.AboutSun) {
            //show help alert dialog
            new AlertDialog.Builder(this)
                    .setTitle(R.string.help)
                    .setMessage(R.string.sunrise_sunset_help)
                    .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                    .show();
        } else if(item.getItemId() == R.id.lovation_favourite_item){
            Intent intent=new Intent(this,SavedLocationsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * An adapter class for the RecyclerView that displays latitude and longitude pairs of saved locations.
     */
    public static class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
        private  final List<String> types;
        private final List<String> times;

        public LocationAdapter(List<String> types,List<String>times) {
            this.types = types;
            this.times=times;
        }

        @NonNull
        @Override
        public LocationAdapter.LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sunrise_sunset, parent, false);
            return new LocationAdapter.LocationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(LocationAdapter.LocationViewHolder holder, int position) {
            String type = types.get(position);
            String time = times.get(position);
            holder.bind(type,time);
        }

        @Override
        public int getItemCount() {
            return times.size();
        }

        static class LocationViewHolder extends RecyclerView.ViewHolder {

            TextView typeTextView;
            TextView timeTextView;

            public LocationViewHolder(View itemView) {
                super(itemView);
                typeTextView = itemView.findViewById(R.id.typeTextView);
                timeTextView =itemView.findViewById(R.id.wordTextView);
            }

            public void bind(String type,String times) {
                typeTextView.setText(type);
                timeTextView.setText(times);
            }
        }
    }
}
