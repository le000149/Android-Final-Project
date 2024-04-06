package algonquin.cst2335.testsun;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FavoriteCities extends AppCompatActivity implements CityAdapter.OnCityInteractionListener{
    private RecyclerView recyclerView;
    private CityAdapter cityAdapter;
    private AppDatabase db;
    private List<City> backedUpCities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites); // Make sure this is your layout

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // Set the Toolbar to act as the ActionBar

        recyclerView = findViewById(R.id.favoritesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "fileOnYourPhone").build();
        loadFavoriteCities();
    }

    private void loadFavoriteCities() {
        db.cityDao().getAllCities().observe(this, cities -> {
            cityAdapter = new CityAdapter(this, cities, db.cityDao());
            cityAdapter.setOnCityInteractionListener(this);
            recyclerView.setAdapter(cityAdapter);
        });
    }

    @Override
    public void onCityDelete(City city) {
        confirmDelCity(city);
    }

    void confirmDelCity(City city) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_deletion)
                .setMessage(getString(R.string.are_you_sure_delete_city, city.getName())) // Use getString to format the message
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteCity(city)) // Use string resource for the "Delete" button
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void deleteCity(City city) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Backup the city in case the user wants to undo the delete
            final City deletedCity = city;

            // Delete the city from the database
            db.cityDao().deleteCity(city.getName()); // Adjust this line to match your DAO's delete method

            // After deletion, show a Snackbar with an Undo option
            runOnUiThread(() -> {
                cityAdapter.removeCity(city);

                Toast.makeText(FavoriteCities.this, R.string.city_deleted, Toast.LENGTH_SHORT).show();

                Snackbar.make(recyclerView, R.string.city_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, v -> undoDelete(deletedCity))
                        .show();
            });
        });
    }

    private void undoDelete(City city) {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Re-insert the city into the database
            db.cityDao().insert(city);

            // Update the UI after undoing the delete
            runOnUiThread(() -> {
                cityAdapter.addCity(city);
                recyclerView.scrollToPosition(cityAdapter.getItemCount() - 1);
            });
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
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

        } else if (id == R.id.delete_all) {
            confirmDeletion();
            return true;
        } else if (id == R.id.action_view_favorites) {
            // Intent to FavoriteCities itself, which might not be necessary if you're already here
            // Consider handling differently or removing if not needed
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(FavoriteCities.this, MainActivity.class);
            startActivity(intent);
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
        builder.setTitle(R.string.help_title) // Use string resource for the title
                .setMessage(R.string.about) // Use string resource for the message
                .setPositiveButton(R.string.ok, null) // Use string resource for the positive button
                .create()
                .show();
    }
    private void confirmDeletion() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_deletion)
                .setTitle(R.string.confirm_deletion)
                .setMessage(R.string.are_you_sure_delete_all_favorite_cities)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteAllCities())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
    private void deleteAllCities() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Backup the current list of cities before deleting
            backedUpCities = new ArrayList<>(cityAdapter.getCities());

            // Delete all cities from the database
            db.cityDao().deleteAllCities();

            // Perform UI actions after deletion
            runOnUiThread(() -> {
                cityAdapter.setCities(new ArrayList<>()); // Clear the adapter's data set
                Snackbar.make(recyclerView, R.string.all_cities_deleted, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, v -> undoDeleteAll())
                        .show();
            });
        });
    }
    private void undoDeleteAll() {
        Executors.newSingleThreadExecutor().execute(() -> {
            // Re-insert the backed-up cities into the database
            for (City city : backedUpCities) {
                db.cityDao().insert(city);
            }

            // Update the UI after undoing the delete
            runOnUiThread(() -> cityAdapter.setCities(backedUpCities));
        });
    }

}
