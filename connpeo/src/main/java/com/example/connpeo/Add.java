package com.example.connpeo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class Add extends AppCompatActivity {

    private ImageView back;
    Handler handler; // 定义一个android.os.Handler对象
    Button takePhoto,chooseFromAlbum;
    private static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView picture;
    List<RecentlyViewed> RecentlyViewedList = new ArrayList<>();
    String img="",param=null;
    private Button dianji;
    private TextView Name, Beiyong,Tel;
    String result=null,id="1";
    private Spinner Fenzu;
    private String[] fenlei = {"同学", "家人", "同事", "其他"};
    String selectedValue;
    Uri imageUri;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //声明一个下拉列表的数组适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fenlei);
        Fenzu = findViewById(R.id.spAnimals);
        //设置下拉框的数组适配器
        Fenzu.setAdapter(adapter);

        //返回事件处理
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Add.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        Name=(EditText)findViewById(R.id.name);
        Beiyong=(EditText)findViewById(R.id.beiyong);
        Tel=(EditText)findViewById(R.id.tel);

        // 创建数据库助手实例
        databaseHelper = new DatabaseHelper(this);

        //点击提交事件处理
        dianji=(Button)findViewById(R.id.dianji);
        dianji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ("".equals(Name.getText().toString())==false||"".equals(Beiyong.getText().toString())==false) {
                    Toast.makeText(Add.this, "添加成功",Toast.LENGTH_SHORT).show();
                    // 获取分组下拉框当前选中的值
                    selectedValue = Fenzu.getSelectedItem().toString();
                    // 将数据插入到数据库中
                    databaseHelper.insertData(Name.getText().toString(), Beiyong.getText().toString(),Tel.getText().toString(),imageUri.toString(),selectedValue);
                    // 返回到主界面 MainActivity
                    Intent intent = new Intent(Add.this, MainActivity.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(Add.this, "添加失败",Toast.LENGTH_SHORT).show();
                    // 显示一个警告对话框，提醒用户信息不能为空
                    new AlertDialog.Builder(Add.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("注意")
                            .setMessage("姓名或备注不能为空")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            }).create().show();
                }
            }
        });


        takePhoto = (Button) findViewById(R.id.take_photo);
        chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);
        picture = (ImageView) findViewById(R.id.picture);

        //选择图片事件处理
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //动态申请读取SD卡权限
                //如果没有权限，通过 ActivityCompat.requestPermissions 方法请求写入外部存储的权限
                if(ContextCompat.checkSelfPermission(Add.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Add.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }
                else{
                    openAlbum();
                }
            }
        });

    }

    //从本地选择图片
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");//将获取内容指定为图片
        startActivityForResult(intent,CHOOSE_PHOTO); //打开相册
    }

    //向请求用户权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            //获取拍照的图片并保存显示
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    picture.setImageBitmap(bitmap);//将该图片设置到 picture 控件中显示
                    imageUri = saveBitmapToFile(Add.this, bitmap);

                    //压缩为png存入stream，再转换为字节数组 bytes，并使用 Base64 编码转换为字符串 img，用于存储或传输
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] bytes = stream.toByteArray();
                    img = Base64.encodeToString(bytes,Base64.DEFAULT);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19){
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            /*如果是document类型的Uri，则通过document id处理*/
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1]; //解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

        displayImage(imagePath); //根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }



    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection){
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if(imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Uri imageUri = saveBitmapToFile(Add.this, bitmap);
            Log.i("imageUri",imageUri.toString());
            picture.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    public static Uri saveBitmapToFile(Context context, Bitmap bitmap) {
        // 获取应用的缓存目录
        File cacheDir = context.getExternalCacheDir();
        File imageFile = new File(cacheDir, "image.jpg");
        try {
            // 创建文件输出流
            FileOutputStream fos = new FileOutputStream(imageFile);
            // 将Bitmap保存到文件中
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // 关闭文件输出流
            fos.close();
            // 获取文件的URI并转换为URL
            Uri uri = Uri.fromFile(imageFile);

            return uri;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}