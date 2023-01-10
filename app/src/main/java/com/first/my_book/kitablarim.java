package com.first.my_book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class kitablarim {
    private String kitabadi,kitabsehifesi;
    private Bitmap kitabresmi;

    public kitablarim(){};
    public kitablarim(String kitabadi,String kitabsehifesi,Bitmap kitabresmi){
this.kitabadi=kitabadi;
this.kitabresmi=kitabresmi;
this.kitabsehifesi=kitabsehifesi;
    }



    public void setKitabadi(String kitabadi) {
        this.kitabadi = kitabadi;
    }

    public void setKitabsehifesi(String kitabsehifesi) {
        this.kitabsehifesi = kitabsehifesi;
    }

    public void setKitabresmi(Bitmap kitabresmi) {
        this.kitabresmi = kitabresmi;
    }
    public String getKitabadi() {
        return kitabadi;
    }

    public String getKitabsehifesi() {
        return kitabsehifesi;
    }

    public Bitmap getKitabresmi() {
        return kitabresmi;
    }
    static  public ArrayList<kitablarim> getdata(Context context){
    ArrayList<kitablarim> kitablist =new ArrayList<>();
    ArrayList<String> kitabad=new ArrayList<>();
    ArrayList<String> kitabseh=new ArrayList<>();
    ArrayList<Bitmap> kitabresm =new ArrayList<>();
    try{
        SQLiteDatabase database=context.openOrCreateDatabase("Kitablar",Context.MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("SELECT *FROM kitablar ",null);
        int kitaba=cursor.getColumnIndex("name");
        int kitabs=cursor.getColumnIndex("page");
        int kitabr=cursor.getColumnIndex("picture");
        while(cursor.moveToNext()){
            kitabad.add(cursor.getString(kitaba));
            kitabseh.add(cursor.getString(kitabs));
            byte[]gelenresim1= cursor.getBlob(kitaba);
            Bitmap gelenresim= BitmapFactory.decodeByteArray(gelenresim1,0, gelenresim1.length);
            kitabresm.add(gelenresim);

        }
        cursor.close();
        for (int i=0;i<kitabad.size();i++){
            kitablarim kitab=new kitablarim();
            kitab.setKitabadi(kitabad.get(i));
            kitab.setKitabsehifesi(kitabseh.get(i));
            kitab.setKitabresmi(kitabresm.get(i));
            kitablist.add(kitab);
        }


    }catch (Exception e){
        e.printStackTrace();
    }


        return kitablist;
    }
}

