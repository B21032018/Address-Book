package com.example.connpeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // RecyclerView用于显示浏览的项
    RecyclerView recentlyViewedRecycler;
    RecentlyViewedAdapter recentlyViewedAdapter;
    // 浏览项的列表
    List<RecentlyViewed> ConnpeoRecentlyViewedList = new ArrayList<>();
    SideBar sideBar; // 侧边栏控件
    private ImageView sea, add, export; // 搜索和添加按钮
    private TextView fz; // 分组文本
    private DatabaseHelper databaseHelper;

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
            // 创建Intent跳转到Search活动
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Search.class);
                startActivity(i); // 启动活动
                finish(); // 结束当前活动
            }
        });

        // 初始化导出按钮并设置点击事件
        export = findViewById(R.id.export);
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获取DatabaseHelper实例
                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);

                // 获取数据库的可写副本
                SQLiteDatabase db = dbHelper.getReadableDatabase();

                // 查询my_table中的所有数据
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);

                File exportDir = new File(getExternalFilesDir(null), "DatabaseExports"); // 使用getExternalFilesDir获取私有外部存储路径
                if (!exportDir.exists()) {
                    exportDir.mkdirs();
                }
                File file = new File(exportDir, "my_table_data.txt");

                try {
                    // 创建文件输出流
                    FileOutputStream fos = new FileOutputStream(file);

                    // 写入表头
                    StringBuilder sb = new StringBuilder();
                    String[] columnNames = cursor.getColumnNames();
                    for (String columnName : columnNames) {
                        sb.append(columnName).append("\t");
                    }
                    sb.deleteCharAt(sb.length() - 1); // 移除最后一个制表符
                    sb.append("\n");
                    fos.write(sb.toString().getBytes());

                    // 写入数据行
                    while (cursor.moveToNext()) {
                        sb.setLength(0); // 重置StringBuilder
                        for (int i = 0; i < columnNames.length; i++) {
                            sb.append(cursor.getString(i)).append("\t");
                        }
                        sb.deleteCharAt(sb.length() - 1); // 移除最后一个制表符
                        sb.append("\n");
                        fos.write(sb.toString().getBytes());
                    }

                    // 清理资源
                    cursor.close();
                    fos.close();

                    Toast.makeText(MainActivity.this, "数据已成功导出至: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "导出失败", Toast.LENGTH_SHORT).show();
                }
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

        // 创建数据库助手实例
        databaseHelper = new DatabaseHelper(this);
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
                // 添加到列表
                ConnpeoRecentlyViewedList.add(new RecentlyViewed(name, beiyong, tel, img, fenzu));
                setRecentlyViewedRecycler(ConnpeoRecentlyViewedList); // 设置RecyclerView
                Collections.sort(ConnpeoRecentlyViewedList); // 对列表进行排序
            }
        }).start(); // 启动线程
    }

    // 设置浏览项的RecyclerView
    private void setRecentlyViewedRecycler(List<RecentlyViewed> recentlyViewedDataList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 1); // 设置布局管理器
        recentlyViewedRecycler.setLayoutManager(layoutManager); // 设置RecyclerView的布局管理器
        recentlyViewedAdapter = new RecentlyViewedAdapter(this, recentlyViewedDataList); // 创建适配器
        recentlyViewedRecycler.setAdapter(recentlyViewedAdapter); // 设置适配器

        // 设置侧边栏的选择回调
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < recentlyViewedDataList.size(); i++) {
                    if (selectStr.equalsIgnoreCase(recentlyViewedDataList.get(i).getStart())) {
                        recentlyViewedRecycler.scrollToPosition(i); // 滚动到指定位置
                        return;
                    }
                }
            }
        });
    }
}
