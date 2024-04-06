package algonquin.cst2335.finalproject.recipe;

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

import algonquin.cst2335.finalproject.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Activity for displaying all saved recipes.
 */
public class SeeAllActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private HomeWarningdapter mListAdapter;
    List<CollectModel> list = new ArrayList<>();
    MyDBOpenHelper mhelper;
    SQLiteDatabase db;
    private TextView title;
    private ImageButton backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);

        // selectall();
    }


    private void initView() {

        mhelper = new MyDBOpenHelper(SeeAllActivity.this);
        db = mhelper.getWritableDatabase();
        mRecyclerView = findViewById(R.id.common_recycleView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

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


        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);

            }
        });
        mRecyclerView.setAdapter(mListAdapter);
    }

    private void selectall() {
        // Notify the adapter that the data set has changed
        mListAdapter.notifyDataSetChanged();
        list.clear();
        // Start query with SQL statement and parameters
        Cursor cursor = db.rawQuery("select * from repair", null);
        if (cursor.getCount() != 0) {// Check if the result set has data
            // Query successful
            Toast.makeText(SeeAllActivity.this, "Successful", Toast.LENGTH_SHORT).show();

            // Iterate through the result set, retrieve data, and display it
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
                cursor.moveToNext();
            }
            // Update the adapter data and notify the change
            mListAdapter.setData(list);
            mListAdapter.notifyDataSetChanged();
            cursor.close();
        } else {
            Toast.makeText(SeeAllActivity.this, "You do not have any favorite in the list.", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        initView();
        selectall();
    }
}

