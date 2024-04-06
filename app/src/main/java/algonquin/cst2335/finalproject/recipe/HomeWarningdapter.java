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
 * Adapter class for the RecyclerView used in the home screen to display collected recipes.
 */

public class HomeWarningdapter extends RecyclerView.Adapter<HomeWarningdapter.InnerHolder> {

    private List<CollectModel> mData = new ArrayList<>();
    private OnRecommendItemClickListener mOnRecommendItemClickListener;
    private Context mContext;
    private MyDBOpenHelper mhelper;
    private SQLiteDatabase db;
    /**
     * Constructor for the HomeWarningdapter class.
     *
     * @param context The context in which the adapter will be used.
     */
    public HomeWarningdapter(Context context){
        mContext=context;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spot_cell, parent, false);
        final InnerHolder viewHolder = new InnerHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.itemView.setTag(position);
        holder.setData(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view ) {
                mOnRecommendItemClickListener.onItemClick(position,mData.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    /**
     * Sets the data for the adapter.
     *
     * @param list The list of collected recipes to be displayed.
     */
    public void setData(List<CollectModel> list) {
        if (list != null) {
            mData.clear();
            mData.addAll(list);
        }
        //update UI
        notifyDataSetChanged();
    }

    /**
     * Inner class representing the ViewHolder for the RecyclerView.
     */
    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
        /**
         * Sets the data for the ViewHolder.
         *
         * @param position The position of the item in the RecyclerView.
         */
        public void setData(int position) {
            TextView t_sno=itemView.findViewById(R.id.sno);
            ImageView imageView=itemView.findViewById(R.id.im);
            CollectModel model = mData.get(position);
            t_sno.setText(""+model.getName());
            Glide.with(mContext).load(model.getUrl()).into(imageView);
        }

    }


    public void setOnRecommendItemListener(OnRecommendItemClickListener listener ){
        mOnRecommendItemClickListener = listener;
    }

    public interface OnRecommendItemClickListener{
        /**
         * Called when an item in the RecyclerView is clicked.
         *
         * @param p   The position of the clicked item.
         * @param testmodel  The data associated with the clicked item.
         */
        void onItemClick(int p, CollectModel testmodel);
    }
}
