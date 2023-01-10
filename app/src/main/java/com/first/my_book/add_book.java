package com.first.my_book;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.BitSet;

public class add_book extends AppCompatActivity {
    private ImageView img;
    private EditText txt1, txt2;
    private String k1,k2;
    private int ickod=1,icnkod=0;
    private Bitmap sresim;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        img = (ImageView) findViewById(R.id.img);
        txt1 = (EditText) findViewById(R.id.txt1);
        txt2=findViewById(R.id.txt2);
    }


    public void addbook(View v) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},icnkod);
        }else
        {
            Intent sekilal =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(sekilal,ickod);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==ickod){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Intent sekilal =new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(sekilal,ickod);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==ickod){
            if(resultCode==RESULT_OK && data!=null){
                Uri resmuri=data.getData();
                try {
                   if(Build.VERSION.SDK_INT>=28){
                       ImageDecoder.Source sresm=ImageDecoder.createSource(this.getContentResolver(),resmuri);
                       sresim=ImageDecoder.decodeBitmap(sresm);
                       img.setImageBitmap(sresim);
                   }else{
                       sresim=MediaStore.Images.Media.getBitmap(this.getContentResolver(),resmuri);
                       img.setImageBitmap(sresim);
                   }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressLint("SuspiciousIndentation")
    public void addbook1(View v) {
        k1=txt1.getText().toString();
        k2=txt2.getText().toString();
        if(!TextUtils.isEmpty(k1)){
            if(!TextUtils.isEmpty(k2)){
                ByteArrayOutputStream output=new ByteArrayOutputStream();
                sresim.compress(Bitmap.CompressFormat.PNG,50,output);
                byte[] qeydedilecekresm= output.toByteArray();

                try {
                   SQLiteDatabase database=this.openOrCreateDatabase("Kitablar",MODE_PRIVATE,null);
                   database.execSQL("Create table if not exists kitablar(id,name VARCHAR,page VARCHAR,picture BLOB)");
                   String sqlqueryy="Insert into kitablar (name,page,picture)VALUES(?,?,?) ";
                    SQLiteStatement statement=database.compileStatement(sqlqueryy);
                    statement.bindString(1,k1);
                    statement.bindString(2,k2);
                    statement.bindBlob(3,qeydedilecekresm);
                    statement.execute();
                    temizle();
                    Toast.makeText(this, "ELAVE EDILDI", Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    e.printStackTrace();
                }



            }else
                Toast.makeText(this, "Səhifə sayı boş ola bilməz", Toast.LENGTH_SHORT).show();
        }else
        Toast.makeText(this, "Kitab adı boş ola bilməz", Toast.LENGTH_SHORT).show();
    }
    public void temizle(){
        txt1.setText("");
        txt2.setText("");
        img.setBackgroundResource(R.drawable.book);
    }


}