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

public class Search extends AppCompatActivity {

    //获取布局文件中的EditText、ImageView、RecyclerView等组件
    private EditText namea;
    private DatabaseHelper databaseHelper;
    private String name = "", img = "", beiyong = "", tel = "",fenzu="";
    FenzuAdapter fenzuAdapter;
    private ImageView back;
    RecyclerView recentlyViewedRecycler;
    List<Fenzued> lstRecentlyViewedList = new ArrayList<>();


    //在onCreate方法中，设置布局文件为activity_search。
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        namea = (EditText) findViewById(R.id.name);

        recentlyViewedRecycler = findViewById(R.id.listview);

        //创建一个DatabaseHelper对象，用于操作数据库
        databaseHelper = new DatabaseHelper(this);

        //为返回按钮设置点击事件，跳转回主界面。
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Search.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        TextView cha = (TextView) findViewById(R.id.cha);
        cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = namea.getText().toString();
                Log.i("nnnnnnnnnname", name);

                //创建一个Map对象，将输入框中的文本作为键值对存储
                final Map<String, String> map = new HashMap<String, String>();
                map.put("name", namea.getText().toString());

                //获取输入框中的文本，并判断是否为空，如果为空则弹出提示框
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

                //调用queryDataS方法查询数据库，并将结果存储在dataList中
                //遍历dataList，将数据添加到lstRecentlyViewedList中
                //调用setRecentlyViewedRecycler方法，将数据设置到RecyclerView中
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
                    lstRecentlyViewedList.add(new Fenzued(name, beiyong,  tel, img, fenzu));
                    setRecentlyViewedRecycler(lstRecentlyViewedList);
                }

            }
        });

    }

    //定义setRecentlyViewedRecycler方法，用于设置RecyclerView的布局管理器和适配器
    //确保 RecyclerView 能够正确地显示 fenzuedDataList 中的数据
    private void setRecentlyViewedRecycler(List<Fenzued> fenzuedDataList) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(Search.this,1);
        recentlyViewedRecycler.setLayoutManager(layoutManager);
        fenzuAdapter = new FenzuAdapter(this, fenzuedDataList);
        recentlyViewedRecycler.setAdapter(fenzuAdapter);
    }

}