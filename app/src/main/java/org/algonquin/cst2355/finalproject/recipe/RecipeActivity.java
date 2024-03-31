package org.algonquin.cst2355.finalproject.recipe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.algonquin.cst2355.finalproject.R;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private EditText medit;
    private Button mbtn;
    private RecyclerView mRecyclerView;

    private RecipeAdapter mListAdapter;

    private LinearLayout loadingLinearLayout;
    private LoadingView mLoadingView;
    private TextView mNoDataView;

    private ImageView mwd;
    /**
     * Activity class for searching and displaying recipes.
     */
    // setup API
    public static final String SPOONACULAR_API_URL = "https://api.spoonacular.com/recipes/complexSearch";
    public static final String SPOONACULAR_API_KEY = "e9b57f24693544368d42b9b79bb5f1ed";

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down, this Bundle contains the data it most recently supplied.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        // Set the Toolbar as the ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);

        loadingLinearLayout = findViewById(R.id.line_loading_view);
        mLoadingView =findViewById(R.id.line_chart_loading);
        mNoDataView =findViewById(R.id.line_chart_no_data);
        SharedPreferences sp = getSharedPreferences("SpQing", Context.MODE_PRIVATE);
        // The second parameter: if the value obtained by the first parameter key is null, the value of the second parameter is used instead
        String value = sp.getString("key", "");
        medit=findViewById(R.id.edit);
        if (value != null && !value.isEmpty()) {
            medit.setText(value);
        }
        mbtn=findViewById(R.id.btn);
        mwd=findViewById(R.id.wd);
        mwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RecipeActivity.this,SeeAllActivity.class));
            }
        });
        mRecyclerView=findViewById(R.id.commonRecycleView);
        // Set layout manager
        LinearLayoutManager manager = new LinearLayoutManager(RecipeActivity.this);
        mRecyclerView.setLayoutManager(manager);
        // Set offset
        mListAdapter = new RecipeAdapter(RecipeActivity.this);

        mListAdapter.setOnRecommendItemListener(new RecipeAdapter.OnRecommendItemClickListener() {
            @Override
            public void onItemClick(int p, model.ResultsDTO testmodel) {
                Intent intent = new Intent();
                intent.putExtra("ID", testmodel.getId());
                intent.putExtra("Image", testmodel.getImage());
                intent.putExtra("Title", testmodel.getTitle());
                intent.setClass(RecipeActivity.this, DetailsActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mListAdapter);
        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("SpQing", Context.MODE_PRIVATE);
                sp.edit().putString("key",medit.getText().toString()).apply();
                GetData();
            }
        });
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

        // Create a request queue
        loadingLinearLayout.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        // Construct query parameters
        StringBuilder sb = new StringBuilder(SPOONACULAR_API_URL);
        sb.append("?query=").append(medit.getText().toString());
        sb.append("&apiKey=").append(SPOONACULAR_API_KEY);
        String finalUrl = sb.toString();
        // Create StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    Log.e("leo", "Response: " + response);

                model model = JSON.parseObject(response,model.class);
                List<model.ResultsDTO> list= model.getResults();
                if(list.size() <=0){
                    mLoadingView.setVisibility(View.GONE);
                    mNoDataView.setVisibility(View.VISIBLE);
                }else{
                    loadingLinearLayout.setVisibility(View.GONE);
                }
                // mRecyclerView.invalidate();
                mListAdapter.setData(list);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Load toolbar_menu.xml menu file
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AlertDialog.Builder normalDialog=new AlertDialog.Builder(RecipeActivity.this);
            normalDialog.setTitle("About Us");
            normalDialog.setMessage("Instructions:" +
                    "1. Enter a recipe name in the search field." +
                    "2. Tap the search button to get matching recipes." +
                    "3. Select a recipe to view details." +
                    "4. Use the save button to add a recipe to your favorites." +
                    "5. View saved recipes by tapping 'View Saved Recipes'." +
                    "6. Tap on a saved recipe to view its details." +
                    "7. Tap the remove button to delete a saved recipe.");
            normalDialog.setNegativeButton("back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    finish();
                }
            });
            normalDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}