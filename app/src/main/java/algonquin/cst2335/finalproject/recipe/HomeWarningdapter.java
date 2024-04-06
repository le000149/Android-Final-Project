package algonquin.cst2335.finalproject.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import algonquin.cst2335.finalproject.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for a RecyclerView used in the home screen to display a list of collected recipes.
 * This adapter manages the data model and adapts it to individual entries in the RecyclerView.
 * Each item in the list represents a collected recipe, including its name and associated image.
 */
public class HomeWarningdapter extends RecyclerView.Adapter<HomeWarningdapter.InnerHolder> {

    /**
     * The dataset of collected recipes to be displayed by the RecyclerView.
     */
    private List<CollectModel> mData = new ArrayList<>();

    /**
     * Listener for handling clicks on items within the RecyclerView.
     */
    private OnRecommendItemClickListener mOnRecommendItemClickListener;

    /**
     * The context in which the adapter is operating. Used to inflate layouts and access resources.
     */
    private Context mContext;

    /**
     * Helper for managing a local database of collected recipes.
     */
    private MyDBOpenHelper mhelper;

    /**
     * The database object for performing CRUD operations on the collected recipes.
     */
    private SQLiteDatabase db;

    /**
     * Constructs a new adapter with the specified context.
     *
     * @param context The context in which the adapter will be used.
     */
    public HomeWarningdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spot_cell, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setTag(position);
        holder.setData(position);
        holder.itemView.setOnClickListener(view -> {
            if (mOnRecommendItemClickListener != null) {
                mOnRecommendItemClickListener.onItemClick(position, mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * Updates the dataset of the adapter and refreshes the RecyclerView to display new data.
     *
     * @param list The new list of collected recipes to be displayed.
     */
    public void setData(List<CollectModel> list) {
        this.mData.clear();
        this.mData.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Sets the listener for item click events within the RecyclerView.
     *
     * @param listener The listener to be invoked on item clicks.
     */
    public void setOnRecommendItemListener(OnRecommendItemClickListener listener ){
        this.mOnRecommendItemClickListener = listener;
    }

    /**
     * Interface for receiving click events from items within the RecyclerView.
     */
    public interface OnRecommendItemClickListener{
        void onItemClick(int position, CollectModel model);
    }

    /**
     * ViewHolder class for RecyclerView items. Holds views for individual entries in the RecyclerView.
     */
    public class InnerHolder extends RecyclerView.ViewHolder {

        /**
         * Constructs a new ViewHolder for the given itemView.
         *
         * @param itemView The view that the ViewHolder will manage.
         */
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * Binds data to the views within the ViewHolder.
         *
         * @param position The position of the item within the adapter's dataset.
         */
        public void setData(int position) {
            TextView textView = itemView.findViewById(R.id.sno);
            ImageView imageView = itemView.findViewById(R.id.im);
            CollectModel model = mData.get(position);
            textView.setText(model.getName());
            Glide.with(mContext).load(model.getUrl()).into(imageView);
        }
    }
}
