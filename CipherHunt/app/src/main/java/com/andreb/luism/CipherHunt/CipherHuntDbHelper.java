package com.andreb.luism.CipherHunt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by luism on 02/12/2016.
 */

public class CipherHuntDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CipherHunt.db";

    public CipherHuntDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CipherHuntDbContract.SQL_CREATE_UTILIZADORES);
        db.execSQL(CipherHuntDbContract.SQL_CREATE_DESAFIOS);
        db.execSQL(CipherHuntDbContract.SQL_CREATE_UTILIZADOR_DESAFIO);
        db.execSQL(CipherHuntDbContract.SQL_CREATE_ENIGMAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CipherHuntDbContract.SQL_DELETE_UTILIZADORES);
        db.execSQL(CipherHuntDbContract.SQL_DELETE_DESAFIOS);
        db.execSQL(CipherHuntDbContract.SQL_DELETE_UTILIZADOR_DESAFIO);
        db.execSQL(CipherHuntDbContract.SQL_DELETE_ENIGMAS);
        onCreate(db);
    }
}
