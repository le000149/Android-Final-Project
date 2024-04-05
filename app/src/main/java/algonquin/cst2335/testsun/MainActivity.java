package algonquin.cst2335.testsun;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import algonquin.cst2335.testsun.AppDatabase;
import algonquin.cst2335.testsun.City;
import algonquin.cst2335.testsun.CityAdapter;
import algonquin.cst2335.testsun.FavoriteCities;

public class MainActivity extends AppCompatActivity {

    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private Button lookupButton;
    private TextView dateTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView cityNameTextView;
    private TextView countryNameTextView;
    private RecyclerView favoritesRecyclerView;
    private CityAdapter cityAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();


        setCurrentDate();
        setupButtonListeners();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_help) {
            showHelpDialog();
            return true;
        }else if (id == R.id.about) {
                showAboutDialog();
                return true;

        }else if (id == R.id.action_view_favorites) {
            Intent intent = new Intent(MainActivity.this, FavoriteCities.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_save_city) {
            String city = cityNameTextView.getText().toString();
            if (!city.isEmpty()) {
                showSaveCityDialog(city);
            } else {
                Toast.makeText(MainActivity.this, "No city to save", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.help_title)); // Set your help title here
        builder.setMessage(getString(R.string.help_message)); // Set your help instructions here
        builder.setPositiveButton("OK", null); // Button to dismiss the dialog
        builder.create().show();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.help_title)); // Set your help title here
        builder.setMessage(R.string.about);
        builder.setPositiveButton("OK", null); // Button to dismiss the dialog
        builder.create().show();
    }
    private void bindViews() {
        dateTextView = findViewById(R.id.dateTextView);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        lookupButton = findViewById(R.id.lookupButton);
        sunriseTextView = findViewById(R.id.sunriseTimeTextView);
        sunsetTextView = findViewById(R.id.sunsetTimeTextView);
        cityNameTextView = findViewById(R.id.cityNameTextView);
        countryNameTextView = findViewById(R.id.countryNameTextView);
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);

    }


    private void setupButtonListeners() {
        lookupButton.setOnClickListener(view -> {
            String latitude = latitudeEditText.getText().toString();
            String longitude = longitudeEditText.getText().toString();


            if (!latitude.isEmpty() && !longitude.isEmpty()) {
                try {
                    double lat = Double.parseDouble(latitude);
                    double lng = Double.parseDouble(longitude);
                    performLookup(lat, lng);
                    getCityAndCountryName(lat, lng);
                    saveLocationInPreferences(latitude, longitude);
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Please enter valid latitude and longitude values", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Latitude and longitude cannot be empty", Toast.LENGTH_LONG).show();
            }


        });
    }
    private void setCurrentDate() {
        String currentDate = DateFormat.getDateInstance(DateFormat.LONG).format(new Date());
        dateTextView.setText(currentDate);

    }

    private void getCityAndCountryName(double latitude, double longitude) {
        String apiKey = "pk.f452ae0b3686ec8c6a3e78209466d1d7"; // Use your actual API key here
        String url = "https://us1.locationiq.com/v1/reverse.php?key=" + apiKey + "&lat=" + latitude + "&lon=" + longitude + "&format=json";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject address = response.getJSONObject("address");
                        String city = address.optString("city", "Loc is Not a City");


                        cityNameTextView.setText(city);


                        runOnUiThread(() -> showSaveCityDialog(city));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error parsing JSON for location", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(MainActivity.this, "Error fetching location: " + error.getMessage(), Toast.LENGTH_SHORT).show());
        queue.add(jsonObjectRequest);
    }

    private void performLookup(double latitude, double longitude) {
        String url = "https://api.sunrisesunset.io/json?lat=" + latitude + "&lng=" + longitude + "&date=today";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject results = response.getJSONObject("results");
                        String sunrise = results.getString("sunrise");
                        String sunset = results.getString("sunset");
                        String country = results.getString("timezone");

                        sunriseTextView.setText(getString(R.string.sunrise_time, sunrise));
                        sunsetTextView.setText(getString(R.string.sunset_time, sunset));
                        countryNameTextView.setText(country);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show());
        queue.add(jsonObjectRequest);
    }

    private void showSaveCityDialog(String city) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.save_city)
                .setMessage(getString(R.string.do_you_want_to_save_city, city)) // Use getString to format the message with the city name
                .setPositiveButton("Save", (dialog, which) -> saveCity(city))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void saveCity(String cityName) {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "fileOnYourPhone").allowMainThreadQueries().build();

            // Check if the city already exists in the database
            List<City> existingCities = db.cityDao().getCitiesByName(cityName);
            if (existingCities.isEmpty()) {
                // City does not exist, so save it
                db.cityDao().insert(new City(cityName));
            } else {
                // City already exists, show a message or take appropriate action
                runOnUiThread(() -> {
                    String message = getString(R.string.city_already_exists, cityName);
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }


    // Assuming you have a button or menu item to clear favorites

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("latitude", latitudeEditText.getText().toString());
        editor.putString("longitude", longitudeEditText.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedLatitude = prefs.getString("latitude", "");
        String savedLongitude = prefs.getString("longitude", "");
        latitudeEditText.setText(savedLatitude);
        longitudeEditText.setText(savedLongitude);
    }

    private void saveLocationInPreferences(String latitude, String longitude) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("latitude", latitude);
        editor.putString("longitude", longitude);
        editor.apply();
    }
}