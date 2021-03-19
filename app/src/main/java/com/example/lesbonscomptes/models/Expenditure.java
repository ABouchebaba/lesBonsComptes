package com.example.lesbonscomptes.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lesbonscomptes.db.DbHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Expenditure {

    private final static String TABLE_NAME = "expenditures";

    private Long id;
    private float cost;
    private Date date;
    private Long payerId;

    public Expenditure(Long id, float cost, Date date, Long payerId) {
        this.id = id;
        this.cost = cost;
        this.date = date;
        this.payerId = payerId;
    }

    public Expenditure() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ TABLE_NAME + " ( " +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "cost REAL NOT NULL," +
                    "payer_id" + " INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "FOREIGN KEY (payer_id) references members(id) ON DELETE CASCADE)";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public void save(DbHelper db){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("cost", cost);
        contentValues.put("payer_id", payerId);
        contentValues.put("date", date.toString());

        db.getWritableDatabase()
                .insertWithOnConflict(
                        TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);

    }

    public static Expenditure find(DbHelper db, Long id){
        String[] columns = new String[]{"id", "cost", "payer_id", "date"};
        String[] sArgs = new String[]{""+id};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "id = ?", sArgs, null,null,null);

        // not found
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
        float tmp_cost = cursor.getFloat(cursor.getColumnIndex("cost"));
        Long tmp_payer_id = cursor.getLong(cursor.getColumnIndex("payer_id"));
        String tmp_date = cursor.getString(cursor.getColumnIndex("date"));

        cursor.close();
        return new Expenditure(tmp_id, tmp_cost, new Date(tmp_date) , tmp_payer_id);
    }

    public static List<Expenditure> find(DbHelper db){
        String[] columns = new String[]{"id", "cost", "payer_id", "date"};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, null, null, null,null,null);

        List<Expenditure> expenditures = new ArrayList<>();

        while (cursor.moveToNext()){
            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
            float tmp_cost = cursor.getFloat(cursor.getColumnIndex("cost"));
            Long tmp_payer_id = cursor.getLong(cursor.getColumnIndex("payer_id"));
            String tmp_date = cursor.getString(cursor.getColumnIndex("date"));

            expenditures.add(new Expenditure(tmp_id, tmp_cost, new Date(tmp_date) , tmp_payer_id));
        }
        cursor.close();

        return expenditures;
    }

    public static int delete(DbHelper dbHelper, Long id){
        String[] sArgs = new String[]{""+id};
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "id = ?", sArgs);
    }

    public static int delete(DbHelper dbHelper){
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "1",  null);
    }
}
