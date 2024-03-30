package org.algonquin.cst2355.finalproject.recipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.algonquin.cst2355.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class SeeAllActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HomeWarningdapter mListAdapter;
    List<CollectModel> list = new ArrayList<>();
    MyDBOpenHelper mhelper;//定义一个数据库帮助类对象
    SQLiteDatabase db;//定义一个操作的数据库的类对象
    private TextView title;
    private ImageButton backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        // selectall();
    }


    private void initView() {
        //        title=findViewById(R.id.nav_title);
        //        backward=findViewById(R.id
        //                .button_backward);
        //        backward.setVisibility(View.VISIBLE);
        //        title.setText("My Collection");
        mhelper = new MyDBOpenHelper(SeeAllActivity.this);//实例化数据库帮助类对象
        db = mhelper.getWritableDatabase();//获取数据库的读写权限
        mRecyclerView = findViewById(R.id.common_recycleView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        //设置偏移量
        mListAdapter = new HomeWarningdapter(this);

        mListAdapter.setOnRecommendItemListener(new HomeWarningdapter.OnRecommendItemClickListener() {
            @Override
            public void onItemClick(int p, CollectModel testmodel) {
                Intent intent = new Intent();
                intent.putExtra("ID", testmodel.getRegion());
                intent.putExtra("Image", testmodel.getUrl());
                intent.putExtra("Title", testmodel.getName());
                intent.setClass(SeeAllActivity.this, DetailsCollectActivity.class);
                startActivity(intent);
            }

        });

        //设置偏移量
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                //                outRect.top= UIUtil.dip2px(view.getContext(),5);
                //                outRect.bottom= UIUtil.dip2px(view.getContext(),5);
                //   outRect.left = UIUtil.dip2px(view.getContext(),10);
                //   outRect.right = UIUtil.dip2px(view.getContext(),10);
            }
        });
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void selectall() {
        mListAdapter.notifyDataSetChanged();
        list.clear();
        //开始查询 参数：（实现查询的 sql 语句，条件参数）
        Cursor cursor = db.rawQuery("select * from repair", null);
        if (cursor.getCount() != 0) {//判断结果集中是否有数据，有：查询成功；无：查询失败
            Toast.makeText(SeeAllActivity.this, "query was successful", Toast.LENGTH_SHORT).show();
            //循环遍历结果集，取出数据，显示出来
            //移动到首位
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                @SuppressLint("Range") String mname = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String mregion = cursor.getString(cursor.getColumnIndex("region"));
                @SuppressLint("Range") String mproblem = cursor.getString(cursor.getColumnIndex("url"));
                CollectModel model = new CollectModel();
                model.setName(mname);
                model.setRegion(mregion);
                model.setUrl(mproblem);
                list.add(model);
                //移动到下一位
                cursor.moveToNext();
            }
            mListAdapter.setData(list);
            mListAdapter.notifyDataSetChanged();
            cursor.close();
        } else {
            Toast.makeText(SeeAllActivity.this, "No favorite information found", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        initView();
        selectall();
    }
}

