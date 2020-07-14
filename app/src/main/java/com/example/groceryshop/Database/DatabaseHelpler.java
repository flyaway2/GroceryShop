package com.example.groceryshop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    public static final String COL_8="TailleAr";
    public static final String COL_9="NomAr";
    public static final String COL_10="MarqueAr";
    public static final String COL_11="Promotion";
    public static final String COL_12="QteRemise";
    public static final String COL_13="RemisePrix";
    public static final String COL_14="Img";
    public static final String COL_15="Username";


    public DatabaseHelpler(@Nullable Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" ("+COL_1+" VARCHAR(30)  ,"+COL_2+" VARCHAR(30),"+COL_3+
               " VARCHAR(30),"+COL_4+" VARCHAR(30),"+COL_5+" REAL,"+COL_6+"  VARCHAR(30),"+COL_7+" INTEGER,"
                +COL_8+" VARCHAR(30),"+COL_9+" VARCHAR(30),"+COL_10+" VARCHAR(30),"+COL_11+" REAL,"+COL_12+
                " INTEGER,"+COL_13+" REAL,"+COL_14+" VARCHAR(30),"+COL_15+" VARCHAR(30), PRIMARY KEY ("+COL_1+","+COL_15+"))"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String codeProd,String Nom,String Marque,float prix,String Taille,int Qte
    ,String TailleAr,String NomAr,String MarqueAr,float Promotion,int QteRemise,float RemisePrix
    ,String Img,String Username){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,codeProd);
        contentValues.put(COL_2,Nom);

        contentValues.put(COL_4,Marque);
        contentValues.put(COL_5,prix);
        contentValues.put(COL_6,Taille);

        contentValues.put(COL_7,Qte);
        contentValues.put(COL_8,TailleAr);
        contentValues.put(COL_9,NomAr);
        contentValues.put(COL_10,MarqueAr);
        contentValues.put(COL_11,Promotion);
        contentValues.put(COL_12,QteRemise);
        contentValues.put(COL_13,RemisePrix);
        contentValues.put(COL_14,Img);
        contentValues.put(COL_15,Username);
        long result=db.insert(TABLE_NAME,null,contentValues);
        return result==1;

    }
    public Cursor getAllData(String Username){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME+" where Username=?",new String[]{Username});


        return  res;

    }
    public boolean updateQte(String CodProd,int Qte,String Username){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_7,Qte);

       int b= db.update(TABLE_NAME,contentValues,COL_1+"=? And "+COL_15+"=?",new String[]{CodProd,Username});
        return b!=0;


    }
    public Integer deleteData(String CodProd,String Username){
        SQLiteDatabase db=this.getWritableDatabase();
        int b=db.delete(TABLE_NAME,"CodeProd=? And "+COL_15+"=?",new String[]{CodProd,Username});
        return b;
    }
    public Integer deleteAll(String Username){
        SQLiteDatabase db=this.getWritableDatabase();
        int b=db.delete(TABLE_NAME,"Username=?",new String[]{Username});
        return b;
    }
}
