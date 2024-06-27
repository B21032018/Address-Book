package com.example.connpeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Fenzu extends AppCompatActivity {

    // RecyclerView用于展示最近查看的列表
    RecyclerView recentlyViewedRecycler;

    // 适配器，用于将数据绑定到RecyclerView
    FenzuAdapter fenzuAdapter;

    // 数据库帮助类，用于操作数据库
    private DatabaseHelper databaseHelper;

    private final String id="同学";// 定义一个输入id的编辑框组件

    // 创建一个ArrayList来存储Fenzued对象列表
    java.util.List<Fenzued> ConnpeoFenzuedList = new ArrayList<>();
    private TextView b,c,d,zm;
    private ImageView sea,add;//图标跳转

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fenzu);

        sea = findViewById(R.id.sea);
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu.this, Search.class);
                startActivity(i);
                finish();
            }
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu.this, Add.class);
                startActivity(i);
                finish();
            }
        });

        zm = findViewById(R.id.zm);
        zm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        b = findViewById(R.id.b);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu.this, Fenzu2.class);
                startActivity(i);
                finish();
            }
        });
        c = findViewById(R.id.c);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu.this, Fenzu3.class);
                startActivity(i);
                finish();
            }
        });
        d = findViewById(R.id.d);
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Fenzu.this, Fenzu4.class);
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
            for (int i = 0; i < dataList.size(); i++) {
                Fenzued fenzued = dataList.get(i);
                // 访问 fenzued 对象的属性
                String name = fenzued.getName();
                String beiyong = fenzued.getbeiyong();
                String tel = fenzued.gettel();
                String img = fenzued.getimg();
                String fenzu = fenzued.getfenzu();
                // 从持久化存储中检索数据以在应用的用户界面（UI）上显示
                ConnpeoFenzuedList.add(new Fenzued(name, beiyong,  tel, img, fenzu));
                setRecentlyViewedRecycler(ConnpeoFenzuedList);
            }
        }).start();
    }

    //设置RecyclerView的布局管理器、适配器和数据
    private void setRecentlyViewedRecycler(List<Fenzued> fenzuedDataList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Fenzu.this,1);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        fenzuAdapter = new FenzuAdapter(this, fenzuedDataList);
        recentlyViewedRecycler.setAdapter(fenzuAdapter);
    }

}