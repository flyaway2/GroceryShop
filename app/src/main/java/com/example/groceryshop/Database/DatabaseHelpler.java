package com.example.groceryshop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelpler extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="groceryshop.db";
    public static final String TABLE_NAME="groceryshop";
    public static final String COL_1="CodeProd";
    public static final String COL_2="Nom";
    public static final String COL_3="Categorie";
    public static final String COL_4="Marque";
    public static final String COL_5="prix";
    public static final String COL_6="Taille";

    public static final String COL_7="Qte";
    public DatabaseHelpler(@Nullable Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" ("+COL_1+" VARCHAR(30) PRIMARY KEY ,"+COL_2+" VARCHAR(30),"+COL_3+
               " VARCHAR(30),"+COL_4+" VARCHAR(30),"+COL_5+" BIGINT(10),"+COL_6+"  VARCHAR(30),"+COL_7+" INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String codeProd,String Nom,String Marque,int prix,String Taille,int Qte){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,codeProd);
        contentValues.put(COL_2,Nom);

        contentValues.put(COL_4,Marque);
        contentValues.put(COL_5,prix);
        contentValues.put(COL_6,Taille);

        contentValues.put(COL_7,Qte);
        long result=db.insert(TABLE_NAME,null,contentValues);
        return result==1;

    }
    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME,null);

        return  res;

    }
    public boolean updateQte(String CodProd,int Qte){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_7,Qte);

       int b= db.update(TABLE_NAME,contentValues,COL_1+"=?",new String[]{CodProd});
        return b!=0;


    }
    public Integer deleteData(String CodProd){
        SQLiteDatabase db=this.getWritableDatabase();
        int b=db.delete(TABLE_NAME,"CodeProd=?",new String[]{CodProd});
        return b;
    }
    public Integer deleteAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        int b=db.delete(TABLE_NAME,null,null);
        return b;
    }
}
