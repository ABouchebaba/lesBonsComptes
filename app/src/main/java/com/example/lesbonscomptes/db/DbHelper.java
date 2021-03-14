package com.example.lesbonscomptes.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Participant;

public class DbHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "les_bons_comptes";
    private final static int DB_VERSION = 1;

    public DbHelper ( Context context ) {
        super ( context , DB_NAME , null , DB_VERSION ) ;
    }

    @ Override
    public void onCreate ( SQLiteDatabase db ) {
        db.execSQL (Group.createQuery() ) ;
        db.execSQL (Member.createQuery() ) ;
        db.execSQL (Expenditure.createQuery() ) ;
        db.execSQL (Participant.createQuery() ) ;
    }

    @ Override
    public void onUpgrade ( SQLiteDatabase db , int oldVersion , int newVersion ) {

        db.execSQL( Participant.dropQuery() ) ;
        db.execSQL( Expenditure.dropQuery() ) ;
        db.execSQL( Member.dropQuery() ) ;
        db.execSQL( Group.dropQuery() ) ;

        onCreate ( db ) ;
    }
}
