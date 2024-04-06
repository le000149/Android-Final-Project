package algonquin.cst2335.finalproject.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
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
 * Adapter for a RecyclerView that displays recipes. This adapter is responsible for
 * providing views that represent each recipe in a dataset.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.InnerHolder> {

    /**
     * Context in which the adapter is being used.
     */
    private Context mContext;

    /**
     * List of recipes (model objects) that the adapter will display.
     */
    private List<model.ResultsDTO> mModelList = new ArrayList<>();

    /**
     * Listener for handling clicks on items within the RecyclerView.
     */
    private OnRecommendItemClickListener mOnRecommendItemClickListener;

    /**
     * Constructs a new RecipeAdapter.
     *
     * @param context The current context.
     */
    public RecipeAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_cell, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setTag(position);
        holder.setData(position);
        holder.itemView.setOnClickListener(view -> {
            if (mOnRecommendItemClickListener != null) {
                mOnRecommendItemClickListener.onItemClick(position, mModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }

    /**
     * Updates the adapter's dataset and refreshes the RecyclerView.
     *
     * @param rTInfoModelList The new dataset.
     */
    public void setData(List<model.ResultsDTO> rTInfoModelList) {
        mModelList.clear();
        if (rTInfoModelList != null) {
            mModelList.addAll(rTInfoModelList);
        }
        notifyDataSetChanged();
    }

    /**
     * Sets the listener that will handle clicks on items within the RecyclerView.
     *
     * @param listener The listener to be set.
     */
    public void setOnRecommendItemListener(OnRecommendItemClickListener listener) {
        mOnRecommendItemClickListener = listener;
    }

    /**
     * ViewHolder class for RecyclerView items. Holds views for individual entries in the RecyclerView.
     */
    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * Binds data to the views within the ViewHolder.
         *
         * @param position The position of the item within the adapter's dataset.
         */
        @SuppressLint("ResourceAsColor")
        public void setData(int position) {
            TextView label1 = itemView.findViewById(R.id.label1);
            ImageView label3 = itemView.findViewById(R.id.label3);

            model.ResultsDTO model = mModelList.get(position);
            label1.setText(model.getTitle());
            Glide.with(mContext).load(model.getImage()).into(label3);
        }
    }

    /**
     * Interface for receiving click events from items within the RecyclerView.
     */
    public interface OnRecommendItemClickListener {
        /**
         * Called when an item in the RecyclerView is clicked.
         *
         * @param position The position of the clicked item.
         * @param testmodel The model associated with the clicked item.
         */
        void onItemClick(int position, model.ResultsDTO testmodel);
    }
}
