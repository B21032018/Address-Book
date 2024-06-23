package com.example.connpeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search extends AppCompatActivity {

    private EditText namea;// 定义一个输入课程的编辑框组件
    private Handler handler; // 定义一个android.os.Handler对象
    private String result = ""; // 定义一个代表显示内容的字符串
    private DatabaseHelper databaseHelper;
    private String name = "", img = "", beiyong = "", tel = "",fenzu="";
    private RecyclerView listView;
    FenzuAdapter fenzuAdapter;
    private ImageView back;
    java.util.List<Map<String, Object>> List = new ArrayList<Map<String, Object>>();
    RecyclerView recentlyViewedRecycler;
    List<Fenzued> luntanRecentlyViewedList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        namea = (EditText) findViewById(R.id.name); //获取用于输入课程的编辑框组件

        recentlyViewedRecycler = findViewById(R.id.listview);

        // 创建数据库助手实例
        databaseHelper = new DatabaseHelper(this);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Search.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        TextView cha = (TextView) findViewById(R.id.cha);    //获取用于登录的按钮控件
        cha.setOnClickListener(new View.OnClickListener() {  //实现单击查询按钮，发送信息与服务器交互
            @Override
            public void onClick(View v) {
                name = namea.getText().toString();
                Log.i("nnnnnnnnnname", name);

                final Map<String, String> map = new HashMap<String, String>();
                map.put("name", namea.getText().toString());

                //当为空时给出相应提示
                if ("".equals(namea.getText().toString())) {
                    new AlertDialog.Builder(Search.this)
                            .setIcon(R.drawable.tishi)
                            .setTitle("提示")
                            .setMessage("请输入查询信息")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }).create().show();
                    return;
                }

                List<Fenzued> dataList = databaseHelper.queryDataS(name);
                for (int i = 0; i < dataList.size(); i++) {
                    Fenzued fenzued = dataList.get(i);
                    // 访问 fenzued 对象的属性
                    String name = fenzued.getName();
                    String beiyong = fenzued.getbeiyong();
                    String tel = fenzued.gettel();
                    String img = fenzued.getimg();
                    String fenzu = fenzued.getfenzu();
                    // ...
                    luntanRecentlyViewedList.add(new Fenzued(name, beiyong,  tel, img, fenzu));
                    setRecentlyViewedRecycler(luntanRecentlyViewedList);
                }

            }
        });

    }

    private void setRecentlyViewedRecycler(List<Fenzued> fenzuedDataList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Search.this,1);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        fenzuAdapter = new FenzuAdapter(this, fenzuedDataList);
        recentlyViewedRecycler.setAdapter(fenzuAdapter);
    }

}