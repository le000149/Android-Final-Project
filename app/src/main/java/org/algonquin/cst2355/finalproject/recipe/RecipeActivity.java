package org.algonquin.cst2355.finalproject.recipe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    // 定义API常量
    public static final String SPOONACULAR_API_URL = "https://api.spoonacular.com/recipes/complexSearch";
    public static final String SPOONACULAR_API_KEY = "fbaec43935c24daba666c7b3b6afd0c3";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置Toolbar为ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        loadingLinearLayout = findViewById(R.id.line_loading_view);
        mLoadingView =findViewById(R.id.line_chart_loading);
        mNoDataView =findViewById(R.id.line_chart_no_data);
        SharedPreferences sp = getSharedPreferences("SpQing", Context.MODE_PRIVATE);
        //第2个参数:表示如果第一个参数的key获取到的值是null,就用第2个参数的值代替
        String value = sp.getString("搜索词", "");
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
        //2 设置布局管理器
        LinearLayoutManager manager = new LinearLayoutManager(RecipeActivity.this);
        mRecyclerView.setLayoutManager(manager);
        //设置偏移量
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
                sp.edit().putString("搜索词",medit.getText().toString()).apply();
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

        // 创建请求队列
        loadingLinearLayout.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        // 构建查询参数
        StringBuilder sb = new StringBuilder(SPOONACULAR_API_URL);
        sb.append("?query=").append(medit.getText().toString());
        sb.append("&apiKey=").append(SPOONACULAR_API_KEY);
        String finalUrl = sb.toString();
        // 创建StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    Log.e("leo", "Response: " + response);
                // 处理响应数据
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
                    // 处理网络错误
                } else if (error instanceof ServerError) {
                    // 处理服务器错误
                } else if (error instanceof TimeoutError) {
                    // 处理超时错误
                } else if (error instanceof NoConnectionError) {
                    // 处理无连接错误
                } else if (error instanceof AuthFailureError) {
                    // 处理认证失败错误
                }
            }
        });
        mQueue.add(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 加载toolbar_menu.xml菜单文件
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AlertDialog.Builder normalDialog=new AlertDialog.Builder(RecipeActivity.this);
            normalDialog.setTitle("About Us");
            normalDialog.setMessage("This is a text introduction about us");
            normalDialog.setNegativeButton("cancellation", new DialogInterface.OnClickListener() {
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