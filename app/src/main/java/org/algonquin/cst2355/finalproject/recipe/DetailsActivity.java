package org.algonquin.cst2355.finalproject.recipe;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.material.snackbar.Snackbar;

import org.algonquin.cst2355.finalproject.R;

public class DetailsActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView mdetails_Summary,mdetails_Spoonacular_Source_Url;
    private LinearLayout loadingLinearLayout;
    private LoadingView mLoadingView;
    private TextView mNoDataView;
    private Integer id;

    private Button mbtn;
    private static final String BASE_URL = "https://api.spoonacular.com/recipes/";
    private static final String API_KEY = "fbaec43935c24daba666c7b3b6afd0c3";
    private static final String RECIPE_ID = "511728"; // could replace the id of the recipe

    private MyDBOpenHelper mhelper;//Define database helper class object
    private SQLiteDatabase db;//Define a database object for operation

    private String url;
    private String Image,Title;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mhelper=new MyDBOpenHelper(DetailsActivity.this);//Instantiate database helper class
        db=mhelper.getWritableDatabase();//Create a database and get read and write permissions for the database
        loadingLinearLayout = findViewById(R.id.line_loading_view);
        mLoadingView =findViewById(R.id.line_chart_loading);
        mNoDataView =findViewById(R.id.line_chart_no_data);
        imageView=findViewById(R.id.details_image);
        mdetails_Summary=findViewById(R.id.details_Summary);
        mdetails_Spoonacular_Source_Url=findViewById(R.id.details_Spoonacular_Source_Url);
        mbtn=findViewById(R.id.details_btn);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=new ContentValues();
                values.put("name",Title);
                values.put("region",id);
                values.put("url",Image);
                db.insert("repair",null,values);
                Snackbar.make(view, "Collection successful!", Snackbar.LENGTH_SHORT).show();
            }
        });
        //Get parameters passed by FirstActivity using Intent object
        Intent intent = getIntent();
        id = intent.getIntExtra("ID",0);
        Image = intent.getStringExtra("Image");
        Title = intent.getStringExtra("Title");
        GetData();
    }

    private void GetData() {
        //        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        //        StringRequest stringRequest_get = new StringRequest("https://api.spoonacular.com/recipes/complexSearch?query=pasta&apiKey=fbaec43935c24daba666c7b3b6afd0c3",
        //                new Response.Listener<String>() {
        //                    @Override
        //                    public void onResponse(String response) {
        //                        Log.e("TAG", response);
        //                    }
        //                }, new Response.ErrorListener() {
        //            @Override
        //            public void onErrorResponse(VolleyError error) {
        //                Log.e("TAG", error.getMessage(), error);
        //            }
        //        });
        //        mQueue.add(stringRequest_get);

        // Create request queue
        loadingLinearLayout.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        // Build query parameters
        String finalUrl = BASE_URL + id + "/information?apiKey=" + API_KEY;
        // Create StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    Log.e("leo", "Response: " + response);
                // Process response data
                details_model model = JSON.parseObject(response,details_model.class);
                url= model.getImage();
                loadingLinearLayout.setVisibility(View.GONE);
                mdetails_Summary.setText(model.getSummary());
                mdetails_Spoonacular_Source_Url.setText(model.getSpoonacularSourceUrl());
                Glide.with(DetailsActivity.this).load(model.getImage()).into(imageView);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("SpoonacularAPI", "Error: " + error.getMessage(), error);
                if (error instanceof NetworkError) {
                    // handle network error
                } else if (error instanceof ServerError) {
                    // handle server error
                } else if (error instanceof TimeoutError) {
                    // handle time out
                } else if (error instanceof NoConnectionError) {

                } else if (error instanceof AuthFailureError) {
                    // handle authorazation error
                }
            }
        });
        mQueue.add(stringRequest);
    }
}
