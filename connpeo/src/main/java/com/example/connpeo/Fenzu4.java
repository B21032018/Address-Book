package com.example.connpeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Fenzu4 extends AppCompatActivity {

    RecyclerView recentlyViewedRecycler;
    FenzuAdapter fenzuAdapter;
    private LinearLayout layoutIndex;
    private DatabaseHelper databaseHelper;
    private String id = "其他"; // 定义一个输入id的编辑框组件
    private Handler handler; // 定义一个android.os.Handler对象
    private String result = "", params = ""; // 定义一个代表显示内容的字符串
    private String name = "", beiyong = "", tel = "", img, fenzu;
    List<Fenzued> luntanFenzuedList = new ArrayList<>();
    List<Map<String, Object>> List = new ArrayList<Map<String, Object>>();
    private TextView a, c, b, zm;
    private ImageView sea, add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenzu4);

        sea = findViewById(R.id.sea);
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu4.this, Search.class);
                startActivity(i);
                finish();
            }
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu4.this, Add.class);
                startActivity(i);
                finish();
            }
        });

        zm = findViewById(R.id.zm);
        zm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu4.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        b = findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu4.this, Fenzu2.class);
                startActivity(i);
                finish();
            }
        });

        c = findViewById(R.id.c);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu4.this, Fenzu3.class);
                startActivity(i);
                finish();
            }
        });

        a = findViewById(R.id.a);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu4.this, Fenzu.class);
                startActivity(i);
                finish();
            }
        });

        recentlyViewedRecycler = findViewById(R.id.recently_item);

        // 创建数据库助手实例
        databaseHelper = new DatabaseHelper(this);

        // 查询数据
        new Thread(() -> {
            List<Fenzued> dataList = databaseHelper.queryData(id);
            if (dataList != null && !dataList.isEmpty()) {
                for (Fenzued fenzued : dataList) {
                    // 访问 fenzued 对象的属性
                    String name = fenzued.getName();
                    String beiyong = fenzued.getbeiyong();
                    String tel = fenzued.gettel();
                    String img = fenzued.getimg();
                    String fenzu = fenzued.getfenzu();
                    luntanFenzuedList.add(new Fenzued(name, beiyong, tel, img, fenzu));
                }
                runOnUiThread(() -> setRecentlyViewedRecycler(luntanFenzuedList));
            } else {
                runOnUiThread(() -> Toast.makeText(Fenzu4.this, "No data found for the specified group.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void setRecentlyViewedRecycler(List<Fenzued> fenzuedDataList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Fenzu4.this, 1);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        fenzuAdapter = new FenzuAdapter(this, fenzuedDataList);
        recentlyViewedRecycler.setAdapter(fenzuAdapter);
    }
}
