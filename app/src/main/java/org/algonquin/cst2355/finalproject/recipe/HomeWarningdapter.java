package org.algonquin.cst2355.finalproject.recipe;

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

import org.algonquin.cst2355.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class HomeWarningdapter extends RecyclerView.Adapter<HomeWarningdapter.InnerHolder> {

    private List<CollectModel> mData = new ArrayList<>();
    private OnRecommendItemClickListener mOnRecommendItemClickListener;
    private Context mContext;
    private MyDBOpenHelper mhelper;//定义数据库帮助类对象
    private SQLiteDatabase db;//定义一个可以操作的数据库对象
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

    //设置数据
    public void setData(List<CollectModel> list) {
        if (list != null) {
            mData.clear();
            mData.addAll(list);
        }
        //更新一下UI
        notifyDataSetChanged();
    }


    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

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
        void onItemClick(int p, CollectModel testmodel);
    }
}
