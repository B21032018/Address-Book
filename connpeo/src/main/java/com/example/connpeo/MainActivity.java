package com.example.connpeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

// MainActivity类，继承自AppCompatActivity
public class MainActivity extends AppCompatActivity {

    // RecyclerView用于显示浏览的项
    RecyclerView recentlyViewedRecycler;
    RecentlyViewedAdapter recentlyViewedAdapter;
    private LinearLayout layoutIndex; // 用于显示索引的线性布局

    private final String id = "1"; // 定义一个输入id的编辑框组件
    private Handler handler; // 定义一个android.os.Handler对象
    private final String result = "";
    private final String params = ""; // 定义代表显示内容的字符串
    private final String name = "";
    private final String beiyong = "";
    private final String tel = "";
    private String img;
    private String fenzu; // 定义相关数据字段
    private ImageView sea, add; // 搜索和添加按钮
    List<RecentlyViewed> ConnpeoRecentlyViewedList = new ArrayList<>(); // 浏览项的列表
    List<Map<String, Object>> List = new ArrayList<>(); // 存储数据的列表
    SideBar sideBar; // 侧边栏控件
    private TextView fz; // 分组文本

    // onCreate方法，当活动创建时调用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 设置活动布局

        // 初始化控件
        recentlyViewedRecycler = findViewById(R.id.recently_item);
        sideBar = findViewById(R.id.sidebar);

        // 初始化搜索按钮并设置点击事件
        sea = findViewById(R.id.sea);
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Search.class); // 创建Intent跳转到Search活动
                startActivity(i); // 启动活动
                finish(); // 结束当前活动
            }
        });

        // 初始化添加按钮并设置点击事件
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Add.class); // 创建Intent跳转到Add活动
                startActivity(i); // 启动活动
                finish(); // 结束当前活动
            }
        });

        // 初始化分组文本并设置点击事件
        fz = findViewById(R.id.fz);
        fz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Fenzu.class); // 创建Intent跳转到Fenzu活动
                startActivity(i); // 启动活动
                finish(); // 结束当前活动
            }
        });

       

        // 查询数据
        new Thread(() -> {
            List<RecentlyViewed> dataList = databaseHelper.queryDataA(); // 从数据库查询数据
            for (int i = 0; i < dataList.size(); i++) {
                RecentlyViewed fenzued = dataList.get(i);
                // 访问 fenzued 对象的属性
                String name = fenzued.getName();
                String beiyong = fenzued.getbeiyong();
                String tel = fenzued.gettel();
                String img = fenzued.getimg();
                String fenzu = fenzued.getfenzu();
                // 添加到最近浏览项列表
                ConnpeoRecentlyViewedList.add(new RecentlyViewed(name, beiyong, tel, img, fenzu));
                setRecentlyViewedRecycler(ConnpeoRecentlyViewedList); // 设置RecyclerView
                Collections.sort(ConnpeoRecentlyViewedList); // 对列表进行排序
            }
        }).start(); // 启动线程
    }
}
