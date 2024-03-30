package org.algonquin.cst2355.finalproject.sunrisesunset;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivitySunriseSunsetOutputBinding;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDAO;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
/**
 * Activity for displaying sunrise and sunset times for a given location.
 * Users can save the current location to a database or view the times fetched from an external API.
 */
public class SunriseSunsetResultActivity extends AppCompatActivity {
    // Field descriptions
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    private static final String TAG = "SunriseSunsetResult";
    // Binding and data fields
    ActivitySunriseSunsetOutputBinding binding;
    private ArrayList<Location> locations;
    private LocationDAO lDAO;
    private boolean locationSaved = true;
    private String latitude;
    private String longitude;
    /**
     * Static method to launch this activity with latitude and longitude parameters.
     *
     * @param activity The parent activity from which this activity is being launched.
     * @param latitude The latitude of the location for which to display sunrise and sunset times.
     * @param longitude The longitude of the location for which to display sunrise and sunset times.
     */
    public static void launch(Activity activity, String latitude, String longitude) {
        Intent intent = new Intent(activity, SunriseSunsetResultActivity.class);
        intent.putExtra(LATITUDE, latitude);
        intent.putExtra(LONGITUDE, longitude);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        Log.d(TAG, "onCreate: ");
        binding = ActivitySunriseSunsetOutputBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbarSun);
        lDAO =MainApplication.getLocationDB().LocationDao();
        latitude = getIntent().getStringExtra(LATITUDE);
        longitude = getIntent().getStringExtra(LONGITUDE);

        // Assuming the title to display coordinates for now
        binding.toolbarSun.setTitle("Results for: " + latitude + ", " + longitude);

        // This method should perform the network request and update the UI accordingly
        searchSunriseSunsetTimes(latitude, longitude);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu_sunrise_sunset_output, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_item) {
            // Check if the Intent has the extras for LATITUDE and LONGITUDE
            if(getIntent().hasExtra(LATITUDE) && getIntent().hasExtra(LONGITUDE)) {
                String lat = getIntent().getStringExtra(LATITUDE);
                String longi = getIntent().getStringExtra(LONGITUDE);

                // Ensure lDAO is not null
                if(lDAO != null) {
                    // Assuming Location constructor takes latitude and longitude as parameters
                    Location location = new Location(lat, longi);
                    Executors.newSingleThreadExecutor().execute(() -> {
                        lDAO.saveLocations(location);
                        // Always make UI changes on the main thread
                        runOnUiThread(() -> Toast.makeText(this, R.string.location_saved, Toast.LENGTH_SHORT).show());
                    });
                } else {
                    Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Latitude or Longitude missing", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**
     * Fetches sunrise and sunset times for the specified location from an external API and updates the UI.
     *
     * @param lat The latitude of the location.
     * @param lng The longitude of the location.
     */
    private void searchSunriseSunsetTimes(String lat,String lng) {
       Log.d(TAG, "searchForLocation: " + lat +lng);
        String url = "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng + "&date=today";

       RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,  new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                try {
                   JSONObject results = response.getJSONObject("results");
                   String sunriseUTC = results.getString("sunrise");
                    String sunsetUTC = results.getString("sunset");

                    String sunriseLocal = convertTimeToLocalTimeZone(sunriseUTC);
                    String sunsetLocal = convertTimeToLocalTimeZone(sunsetUTC);
                   updateUI(sunriseLocal, sunsetLocal);
               } catch (JSONException e) {
                    e.printStackTrace();
               }
            }
            }, error -> Log.e(TAG, "Volley error: ", error));

            queue.add(jsonObjectRequest);
        }
    /**
     * Converts a time string from UTC to the local timezone and formats it for display.
     * This method assumes the time provided is for the current date in UTC and converts it
     * to the system's default timezone, formatting the output to a more readable form without seconds.
     *
     * @param utcTime The time string in UTC to be converted. Expected format is "hh:mm:ss a"
     * @return A string representing the converted time in the system's default timezone, formatted as "hh:mm a" (e.g., "10:59 AM"),
     *         using the US locale. Returns the original UTC time string if parsing fails.
     * @throws DateTimeParseException if the utcTime string cannot be parsed into a valid time.
     *                                This exception is caught within the method and logged, and the original
     *                                utcTime string is returned in such cases.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String convertTimeToLocalTimeZone(String utcTime) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.US);
            LocalTime time = LocalTime.parse(utcTime, inputFormatter);
            // Assuming today's date for the conversion (UTC)
            LocalDate today = LocalDate.now(ZoneId.of("UTC"));
            // Combine local time and date, and specify the initial timezone as UTC
            ZonedDateTime utcDateTime = ZonedDateTime.of(today, time, ZoneId.of("UTC"));
            // Convert UTC DateTime to the system's default timezone
            ZonedDateTime localDateTime = utcDateTime.withZoneSameInstant(ZoneId.systemDefault());
            // Output format (e.g., "hh:mm a" for "10:59 AM")
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
            return outputFormatter.format(localDateTime);
        } catch (DateTimeParseException e) {
            Log.e(TAG, "Error parsing time: " + utcTime, e);
            return utcTime; // Return the original time if there's a parsing error
        }
    }
    /**
     * Updates the UI to display the sunrise and sunset times.
     *
     * @param sunrise The sunrise time to display.
     * @param sunset The sunset time to display.
     */
        private void updateUI(String sunrise, String sunset) {
            List<SunriseSunset> sunriseSunsetList = new ArrayList<>();
            sunriseSunsetList.add(new SunriseSunset(sunrise, sunset));

            SunriseSunsetAdapter adapter = new SunriseSunsetAdapter(sunriseSunsetList);
            binding.recyclerSunTime.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerSunTime.setAdapter(adapter);
    }
    /**
     * Adapter class for displaying sunrise and sunset times in a RecyclerView.
     */
    public class SunriseSunsetAdapter extends RecyclerView.Adapter<SunriseSunsetAdapter.ViewHolder> {
        private List<SunriseSunset> data;

        public SunriseSunsetAdapter(List<SunriseSunset> data) {
            this.data = data;
       }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sunrise_sunset_result, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           SunriseSunset item = data.get(position);
            holder.sunriseTextView.setText(item.getSunrise());
            holder.sunsetTextView.setText(item.getSunset());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView sunriseTextView;
            TextView sunsetTextView;

            public ViewHolder(View view) {
                super(view);
                sunriseTextView = view.findViewById(R.id.typeTextView);
               sunsetTextView = view.findViewById(R.id.wordTextView);
            }
        }
    }
}
