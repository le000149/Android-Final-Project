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
 * Adapter class for displaying recipes in a RecyclerView.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.InnerHolder> {

    private Context mContext;
    private List<model.ResultsDTO> mModelList = new ArrayList<>();
    private OnRecommendItemClickListener mOnRecommendItemClickListener;
    public RecipeAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_cell, parent, false);
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
                mOnRecommendItemClickListener.onItemClick(position,mModelList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModelList.size();
    }


    public void setData(List<model.ResultsDTO> rTInfoModelList) {
        mModelList.clear();
        if (rTInfoModelList != null) {
            mModelList.addAll(rTInfoModelList);
        }

        notifyDataSetChanged();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
        @SuppressLint("ResourceAsColor")
        public void setData(int position) {

            TextView label1;
            ImageView label3;
            label1 = itemView.findViewById(R.id.label1);
            label3 = itemView.findViewById(R.id.label3);

            model.ResultsDTO model = mModelList.get(position);
            label1.setText(model.getTitle());
            // label3.setText(""+model.getAppMenuName());
            Glide.with(mContext)
                    .load(model.getImage())
                    .into(label3);
            //  label4.setText(""+model.getNumber());
            //  label5.setText(""+model.getiPPort());

        }

    }

    public void setOnRecommendItemListener(OnRecommendItemClickListener listener ){
        mOnRecommendItemClickListener = listener;
    }

    public interface OnRecommendItemClickListener{
        void onItemClick(int p, model.ResultsDTO testmodel);
    }
}
