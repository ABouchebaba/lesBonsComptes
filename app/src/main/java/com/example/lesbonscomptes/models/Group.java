package com.example.lesbonscomptes.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lesbonscomptes.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final static String TABLE_NAME = "groups";
    private final static String[] COLUMNS = new String[]{
            "id", "name"
    };

    private Long id;
    private String name;

    public Group(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Group() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ TABLE_NAME + " ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        "name TEXT NOT NULL);";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public void save(DbHelper db){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);

        db.getWritableDatabase()
                .insertWithOnConflict(
                        TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);

    }

    public static Group find(DbHelper db, Long id){

        String[] columns = new String[]{"id", "name"};
        String[] sArgs = new String[]{""+id};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "id = ?", sArgs, null,null,null);

        // not found
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
        String tmp_name = cursor.getString(cursor.getColumnIndex("name"));

        cursor.close();

        return new Group(tmp_id, tmp_name);
    }

    public static List<Group> find(DbHelper db){
        String[] columns = new String[]{"id", "name"};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, null, null, null,null,null);

        List<Group> groups = new ArrayList<>();

        while (cursor.moveToNext()){
            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
            String tmp_name = cursor.getString(cursor.getColumnIndex("name"));
            groups.add(new Group(tmp_id, tmp_name));
        }
        cursor.close();

        return groups;
    }

    public static int delete(DbHelper dbHelper, Long id){
        String[] sArgs = new String[]{""+id};
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "id = ?", sArgs);
    }

    public static int delete(DbHelper dbHelper){
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "1",  null);
    }
}
