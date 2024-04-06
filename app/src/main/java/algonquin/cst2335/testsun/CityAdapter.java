package algonquin.cst2335.testsun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private Executor executor = Executors.newSingleThreadExecutor();
    private Context context;
    private List<City> cities;
    private OnCityInteractionListener listener;
    private CityDao cityDao;
    public List<City> getCities() {
        return cities;
    }
    public void removeCity(City city) {
        int position = cities.indexOf(city);
        if (position > -1) {
            cities.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cities.size());
        }
    }
    public void addCity(City city) {
        if (city != null) {
            // Add the city to the list
            cities.add(city);
            // Notify the adapter that an item has been added
            notifyItemInserted(cities.size() - 1);
        }
    }

    public CityAdapter(Context context, List<City> cities, CityDao cityDao) {
        this.context = context;
        this.cities = cities;
        this.cityDao = cityDao;
    }

    public interface OnCityInteractionListener {
        void onCityDelete(City city);
    }

    public void setOnCityInteractionListener(OnCityInteractionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.city_item, parent, false);
        return new CityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City currentCity = cities.get(position);
        holder.cityNameTextView.setText(currentCity.getName());

        holder.overflowMenu.setOnClickListener(view -> showPopupMenu(view, position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
        notifyDataSetChanged();
    }
    class CityViewHolder extends RecyclerView.ViewHolder {
        public TextView cityNameTextView;
        public ImageView overflowMenu;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityNameTextView = itemView.findViewById(R.id.cityNameTextView);
            overflowMenu = itemView.findViewById(R.id.overflow_menu);
        }
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.inflate(R.menu.overflow_menu);
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete_city) {
                City cityToDelete = cities.get(position);
                new AlertDialog.Builder(context)

                        .setTitle(R.string.delete_city) // Use string resource for the title
                        .setMessage(context.getString(R.string.are_you_sure_delete_city, cityToDelete.getName())) // Use getString to format the message
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // Delegate the deletion to the listener
                            if (listener != null) {
                                listener.onCityDelete(cityToDelete);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            }
            return false;
        });
        popup.show();
    }
}
