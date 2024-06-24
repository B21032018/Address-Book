package com.example.connpeo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Perdet extends AppCompatActivity {

    private String name,beiyong,tel,img,result="",fenzu;

    // 定义界面上的控件变量
    private TextView Name;
    private EditText Beiyong,Tel,fenzua;
    private ImageView back,Img;


    private DatabaseHelper databaseHelper;

    // 定义一个Bitmap对象，用于存储图片
    private Bitmap imgBitmap = null;


    private Button xiugai,shanchu;


    // 声明一个Spinner对象，用于下拉列表
    private Spinner Fenzu;

    // 定义一个字符串数组，用于Spinner的下拉选项
    private String[] categories = {"同学", "家人", "同事", "其他"};


    // 定义一个字符串变量，用于存储Spinner选中的值
    String selectedValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdet);

        // 从Intent中获取传递过来的数据
        Intent i = getIntent();
        name = i.getStringExtra("name");
        beiyong = i.getStringExtra("beiyong");
        tel = i.getStringExtra("tel");
        img = i.getStringExtra("img");
        fenzu = i.getStringExtra("fenzu");

        // 通过ID查找布局中的控件并赋值给对应的变量
        Name = findViewById(R.id.name);
        Beiyong = findViewById(R.id.beiyong);
        Tel = findViewById(R.id.tel);
        Img = findViewById(R.id.img);
        Fenzu = findViewById(R.id.spAnimals);
        fenzua = findViewById(R.id.fenzu);

        // 将获取到的数据设置到对应的控件上
        Name.setText(name);
        Beiyong.setText(beiyong);
        Tel.setText(tel);
        requestWebPhotoBitmap(img);
        fenzua.setText(fenzu);// 这里设置的是EditText的文本，而不是Spinner的选中项

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建一个Intent跳转到MainActivity
                Intent i = new Intent(Perdet.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // 创建数据库助手实例
        databaseHelper = new DatabaseHelper(this);

        //设置下拉框的数组适配器,显示下拉选项
        Fenzu.setAdapter(adapter);

        xiugai = (Button)findViewById(R.id.xiugai);
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // 获取Fenzu当前选中的项，并将其转换为字符串
                selectedValue = Fenzu.getSelectedItem().toString();

                // 修改数据库中的信息
                databaseHelper.updateData(Name.getText().toString(), Beiyong.getText().toString(),Tel.getText().toString(),selectedValue);

                // 显示一个Toast消息，提示用户联系人修改成功
                Toast.makeText(Perdet.this, "联系人修改成功",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Perdet.this, MainActivity.class);
                startActivity(intent);
            }
        });


        shanchu = (Button)findViewById(R.id.shanchu);
        shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                databaseHelper.deleteData(Name.getText().toString());

                // 显示一个Toast消息，提示用户联系人删除成功
                Toast.makeText(Perdet.this, "联系人删除成功",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Perdet.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 获取 网络图片 Bitmap
     * @param imgUrl 网络图片url
     */
    private void requestWebPhotoBitmap(String imgUrl) {
        // 使用Glide库从网络加载图片，并指定为Bitmap格式
        Glide.with(Perdet.this).asBitmap().load(imgUrl).into(new CustomTarget<Bitmap>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            // 当图片资源加载完成时调用此方法
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgBitmap = resource;
                Img.setImageBitmap(imgBitmap);
            }
            @Override
            // 当加载的图片被清除时调用此方法
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }
}}