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

public class DetailsCollectActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView mdetails_Summary,mdetails_Spoonacular_Source_Url;
    private LinearLayout loadingLinearLayout;
    private LoadingView mLoadingView;
    private TextView mNoDataView;
    private Integer id;

    private Button mbtn;
    private static final String BASE_URL = "https://api.spoonacular.com/recipes/";
    private static final String API_KEY = "fbaec43935c24daba666c7b3b6afd0c3";
    private static final String RECIPE_ID = "511728"; // 这个是可以替换的食谱ID

    private MyDBOpenHelper mhelper;//定义数据库帮助类对象
    private SQLiteDatabase db;//定义一个可以操作的数据库对象

    private String url,Title;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_collect);
        mhelper=new MyDBOpenHelper(DetailsCollectActivity.this);//实例化数据库帮助类
        db=mhelper.getWritableDatabase();//创建数据库，获取数据库的读写权限
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
                normalDialog.setTitle("prompt");
                normalDialog.setMessage("Are you sure to delete this collection？");
                normalDialog.setPositiveButton("determine", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //怎么样删除呢？参数：（表名，删除的条件，条件的参数）
                        db.delete("repair","name=?",new String[]{Title});
                        Toast.makeText(DetailsCollectActivity.this,"Delete successful",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                normalDialog.setNegativeButton("cancellation", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(DetailsCollectActivity.this,"Canceled",Toast.LENGTH_SHORT).show();
                    }
                });

                normalDialog.show();
            }
        });
        //使用Intent对象得到FirstActivity传递来的参数
        Intent intent = getIntent();
        id = Integer.valueOf(intent.getStringExtra("ID"));
        Title = intent.getStringExtra("Title");
        GetData();
    }

    private void GetData() {
        // 创建请求队列
        loadingLinearLayout.setVisibility(View.VISIBLE);
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        // 构建查询参数
        String finalUrl = BASE_URL + id + "/information?apiKey=" + API_KEY;
        // 创建StringRequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("leo", "Response: " + response);
                // 处理响应数据
                details_model model = JSON.parseObject(response,details_model.class);
                url= model.getImage();
                loadingLinearLayout.setVisibility(View.GONE);
                mdetails_Summary.setText(model.getSummary());
                mdetails_Spoonacular_Source_Url.setText(model.getSpoonacularSourceUrl());
                Glide.with(DetailsCollectActivity.this).load(model.getImage()).into(imageView);

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
}
