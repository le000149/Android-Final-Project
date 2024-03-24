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

import org.algonquin.cst2355.finalproject.MainApplication;
import org.algonquin.cst2355.finalproject.R;
import org.algonquin.cst2355.finalproject.databinding.ActivitySunriseSunsetOutputBinding;
import org.algonquin.cst2355.finalproject.sunrisesunset.accesslayer.LocationDAO;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SunriseSunsetResultActivity extends AppCompatActivity {
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    private static final String TAG = "SunriseSunsetResult";

    ActivitySunriseSunsetOutputBinding binding;
    private ArrayList<Location> locations;
    private LocationDAO lDAO;
    private boolean locationSaved = true;
    private String latitude;
    private String longitude;

    public static void launch(Activity activity, String latitude, String longitude) {
        Intent intent = new Intent(activity, SunriseSunsetResultActivity.class);
        intent.putExtra(LATITUDE, latitude);
        intent.putExtra(LONGITUDE, longitude);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
           lDAO.saveLocations(new Location(latitude, longitude));
           Toast.makeText(this, "Location saved", Toast.LENGTH_SHORT).show();
       }
        return true;
    }

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

                   updateUI(sunriseUTC, sunsetUTC);
               } catch (JSONException e) {
                    e.printStackTrace();
               }
            }
            }, error -> Log.e(TAG, "Volley error: ", error));

            queue.add(jsonObjectRequest);
        }

        private void updateUI(String sunrise, String sunset) {
            List<SunriseSunset> sunriseSunsetList = new ArrayList<>();
            sunriseSunsetList.add(new SunriseSunset(sunrise, sunset)); // Tạo danh sách mới với kết quả

            SunriseSunsetAdapter adapter = new SunriseSunsetAdapter(sunriseSunsetList);
            binding.recyclerSunTime.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerSunTime.setAdapter(adapter);
    }

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
               sunsetTextView = view.findViewById(R.id.timeTextView);
            }
        }
    }
}
