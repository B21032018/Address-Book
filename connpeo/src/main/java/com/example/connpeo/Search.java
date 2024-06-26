package com.example.connpeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Search extends AppCompatActivity {

    private EditText nameEditText;
    private DatabaseHelper databaseHelper;
    private RecyclerView recentlyViewedRecycler;
    private List<Fenzued> lstRecentlyViewedList = new ArrayList<>();
    private FenzuAdapter fenzuAdapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 初始化界面组件
        nameEditText = findViewById(R.id.name);
        recentlyViewedRecycler = findViewById(R.id.listview);
        back = findViewById(R.id.back);

        // 创建数据库操作对象
        databaseHelper = new DatabaseHelper(this);

        // 返回按钮点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToMainActivity();
            }
        });

        // 查询按钮点击事件
        TextView searchButton = findViewById(R.id.cha);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchName = nameEditText.getText().toString().trim();

                // 输入验证
                if (searchName.isEmpty()) {
                    showEmptyInputAlert();
                    return;
                }

                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(() -> {
                    List<Fenzued> dataList = databaseHelper.queryDataS(searchName);
                    List<Fenzued> updatedList = new ArrayList<>();

                    // 批量处理数据
                    for (Fenzued fenzued : dataList) {
                        String name = fenzued.getName();
                        String beiyong = fenzued.getbeiyong();
                        String tel = fenzued.gettel();
                        String img = fenzued.getimg();
                        String fenzu = fenzued.getfenzu();
                        updatedList.add(new Fenzued(name, beiyong, tel, img, fenzu));
                    }

                    // 在UI线程更新RecyclerView
                    runOnUiThread(() -> setRecentlyViewedRecycler(updatedList));
                });

                // 关闭线程池
                executor.shutdown();
            }
        });
    }

    // 设置 RecyclerView
    private void setRecentlyViewedRecycler(List<Fenzued> fenzuedDataList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        fenzuAdapter = new FenzuAdapter(this, fenzuedDataList);
        recentlyViewedRecycler.setAdapter(fenzuAdapter);
    }

    // 返回主界面
    private void navigateToMainActivity() {
        Intent intent = new Intent(Search.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 显示空输入提示
    private void showEmptyInputAlert() {
        new AlertDialog.Builder(Search.this)
                .setIcon(R.drawable.tishi)
                .setTitle("提示")
                .setMessage("请输入查询信息")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 可以添加处理空输入的逻辑，如聚焦输入框
                    }
                }).create().show();
    }
}
