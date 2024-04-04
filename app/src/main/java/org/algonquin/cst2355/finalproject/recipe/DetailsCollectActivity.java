package org.algonquin.cst2355.finalproject.recipe;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.algonquin.cst2355.finalproject.R;
/**
 * This activity displays the details of a collected recipe and provides an option to delete it.
 */
public class DetailsCollectActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView mdetails_Summary,mdetails_Spoonacular_Source_Url;
    private LinearLayout loadingLinearLayout;
    private LoadingView mLoadingView;
    private TextView mNoDataView;
    private Integer id;

    private Button mbtn;
    private static final String BASE_URL = "https://api.spoonacular.com/recipes/";
    private static final String API_KEY = "3db883fb7d4f4dcfadbeca2505f7128e";
    private static final String RECIPE_ID = "511728";

    private MyDBOpenHelper mhelper;
    private SQLiteDatabase db;

    private String url,Title;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_collect);
        mhelper=new MyDBOpenHelper(DetailsCollectActivity.this);
        db=mhelper.getWritableDatabase();
        loadingLinearLayout = findViewById(R.id.line_loading_view2);
        mLoadingView =findViewById(R.id.line_chart_loading2);
        mNoDataView =findViewById(R.id.line_chart_no_data2);
        imageView=findViewById(R.id.details_image2);
        mdetails_Summary=findViewById(R.id.details_Summary2);
        mdetails_Spoonacular_Source_Url=findViewById(R.id.details_Spoonacular_Source_Url2);
        mbtn=findViewById(R.id.details_btn2);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder normalDialog=new AlertDialog.Builder(DetailsCollectActivity.this);
                normalDialog.setTitle("Caution!");
                normalDialog.setMessage("Are you sure to delete this collection？");
                normalDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        db.delete("repair","name=?",new String[]{Title});
                        Toast.makeText(DetailsCollectActivity.this,"Deleted successful",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                normalDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailsCollectActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
                    }
                });

                normalDialog.show();
            }
        });
        // Retrieve the parameters passed from the FirstActivity using an Intent object.
        Intent intent = getIntent();
        id = Integer.valueOf(intent.getStringExtra("ID"));
        Title = intent.getStringExtra("Title");
        GetData();
    }
    /**
     * Fetches recipe details from the API and updates the UI accordingly.
     */
    private void GetData() {

        loadingLinearLayout.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());

        String finalUrl = BASE_URL + id + "/information?apiKey=" + API_KEY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("leo", "Response: " + response);
                //handle the data
                details_model model = JSON.parseObject(response,details_model.class);
                url= model.getImage();
                loadingLinearLayout.setVisibility(View.GONE);
                //delete<b></b>....
                String summaryWithoutTags = model.getSummary().replaceAll("<[^>]*>", "");
                mdetails_Summary.setText(summaryWithoutTags);

                mdetails_Spoonacular_Source_Url.setText(model.getSpoonacularSourceUrl());
                Glide.with(DetailsCollectActivity.this).load(model.getImage()).into(imageView);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SpoonacularAPI", "Error: " + error.getMessage(), error);
                if (error instanceof NetworkError) {

                } else if (error instanceof ServerError) {

                } else if (error instanceof TimeoutError) {

                } else if (error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {

                }
            }
        });
        mQueue.add(stringRequest);
    }
}
