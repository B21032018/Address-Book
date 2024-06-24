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
    private TextView Name;
    private EditText Beiyong,Tel,fenzua;
    private ImageView back,Img;
    private DatabaseHelper databaseHelper;
    private Bitmap imgBitmap = null;
    private Button xiugai,shanchu;
    private Spinner Fenzu;
    private String[] animals = {"同学", "家人", "同事", "其他"};
    String selectedValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perdet);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        beiyong = i.getStringExtra("beiyong");
        tel = i.getStringExtra("tel");
        img = i.getStringExtra("img");
        fenzu = i.getStringExtra("fenzu");

        Name = findViewById(R.id.name);
        Beiyong = findViewById(R.id.beiyong);
        Tel = findViewById(R.id.tel);
        Img = findViewById(R.id.img);
        Fenzu = findViewById(R.id.spAnimals);
        fenzua = findViewById(R.id.fenzu);

        Name.setText(name);
        Beiyong.setText(beiyong);
        Tel.setText(tel);
        requestWebPhotoBitmap(img);
        fenzua.setText(fenzu);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Perdet.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, animals);

        // 创建数据库助手实例
        databaseHelper = new DatabaseHelper(this);

        //设置下拉框的数组适配器
        Fenzu.setAdapter(adapter);

        xiugai = (Button)findViewById(R.id.xiugai);
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                selectedValue = Fenzu.getSelectedItem().toString();
                Log.i("aaaaaaaaaaaaaaaaaaaaa",selectedValue);
                Log.i("aaaaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaa");
                // 修改数据库中的信息
                databaseHelper.updateData(Name.getText().toString(), Beiyong.getText().toString(),Tel.getText().toString(),selectedValue);
                Toast.makeText(Perdet.this, "联系人修改成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Perdet.this, MainActivity.class);
                startActivity(intent);
            }
        });


        shanchu = (Button)findViewById(R.id.shanchu);
        shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.i("aaaaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaa");
                databaseHelper.deleteData(Name.getText().toString());
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
        Glide.with(Perdet.this).asBitmap().load(imgUrl).into(new CustomTarget<Bitmap>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                imgBitmap = resource;
                Img.setImageBitmap(imgBitmap);
            }
            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }
}