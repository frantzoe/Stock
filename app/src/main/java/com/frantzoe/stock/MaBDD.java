package com.frantzoe.stock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Lincoln on 3/18/2015.
 */

public class MaBDD extends SQLiteOpenHelper {

    private static final int BDD_VERSION = 1;
    private static final String BDD_NOM = "entrepot";
    public static final String TABLE_PRODUITS = "produits";
    public static final String COLUMN_NOM = "nomproduit";
    public static final String COLUMN_QUANTITE = "quantiteproduit";

    public MaBDD(Context context) {
        super(context, BDD_NOM, null, BDD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE " + TABLE_PRODUITS + " (" +
                COLUMN_NOM + " TEXT NOT NULL, " +
                COLUMN_QUANTITE + " INTEGER NOT NULL);";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ///
    }

    public void ajouterProduit(Produit produit) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOM, produit.get_libelle());
        values.put(COLUMN_QUANTITE, produit.get_quantite());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PRODUITS, null, values);
        db.close();
    }

    public void supprimerProduit(String name){
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_PRODUITS + " WHERE " + COLUMN_NOM + " ='" + name + "'");
    }

    public ArrayList<Produit> tousLesProduits(){
        ArrayList<Produit> dbProductsArray = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_PRODUITS + " WHERE 1 ORDER BY " + COLUMN_NOM, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if (cursor.getString(cursor.getColumnIndex(COLUMN_NOM)) != null){
                dbProductsArray.add(new Produit(
                        cursor.getString(cursor.getColumnIndex(COLUMN_NOM)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITE))));
            }
            cursor.moveToNext();
        }
        cursor.close();
        return dbProductsArray;
    }
}

