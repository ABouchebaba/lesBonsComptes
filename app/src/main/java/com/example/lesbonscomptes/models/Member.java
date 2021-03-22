package com.example.lesbonscomptes.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lesbonscomptes.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class Member {

    private final static String TABLE_NAME = "members";

    private Long id;
    private String name;
    private String phone;
    private Long groupId;

    public Member(Long id, String name, String phone, Long groupId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.groupId = groupId;
    }

    public Member() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ TABLE_NAME + " ( " +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                        "name TEXT NOT NULL," +
                        "phone TEXT NOT NULL," +
                        "group_id INTEGER NOT NULL," +
                        "FOREIGN KEY(group_id) references groups(id) ON DELETE CASCADE);";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public Member save(DbHelper db){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("group_id", groupId);

        db.getWritableDatabase()
                .insertWithOnConflict(
                        TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);

        return getLastInserted(db);
    }

    private Member getLastInserted(DbHelper db){

        Cursor cursor = db.getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = last_insert_rowid();", null);

        // not found
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
        String tmp_name = cursor.getString(cursor.getColumnIndex("name"));
        String tmp_phone = cursor.getString(cursor.getColumnIndex("phone"));
        Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));

        cursor.close();

        return new Member(tmp_id, tmp_name, tmp_phone, tmp_group_id);
    }

    public static Member find(DbHelper db, Long id){
        String[] columns = new String[]{"id", "name", "phone", "group_id"};
        String[] sArgs = new String[]{""+id};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "id = ?", sArgs, null,null,null);

        // not found
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
        String tmp_name = cursor.getString(cursor.getColumnIndex("name"));
        String tmp_phone = cursor.getString(cursor.getColumnIndex("phone"));
        Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));

        cursor.close();

        return new Member(tmp_id, tmp_name, tmp_phone, tmp_group_id);
    }

    public static List<Member> find(DbHelper db){
        String[] columns = new String[]{"id", "name", "phone", "group_id"};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, null, null, null,null,null);

        List<Member> members = new ArrayList<>();

        while (cursor.moveToNext()){
            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
            String tmp_name = cursor.getString(cursor.getColumnIndex("name"));
            String tmp_phone = cursor.getString(cursor.getColumnIndex("phone"));
            Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));

            members.add(new Member(tmp_id, tmp_name, tmp_phone, tmp_group_id));
        }
        cursor.close();

        return members;
    }

    public static List<Member> findByGroupId(DbHelper db, Long groupId){
        String[] columns = new String[]{"id", "name", "phone", "group_id"};
        String[] sArgs = new String[]{""+groupId};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "group_id = ?", sArgs, null,null,null);

        List<Member> members = new ArrayList<>();

        while (cursor.moveToNext()){
            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
            String tmp_name = cursor.getString(cursor.getColumnIndex("name"));
            String tmp_phone = cursor.getString(cursor.getColumnIndex("phone"));
            Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));

            members.add(new Member(tmp_id, tmp_name, tmp_phone, tmp_group_id));
        }
        cursor.close();

        return members;
    }

    public static int delete(DbHelper dbHelper, Long id){
        String[] sArgs = new String[]{""+id};
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "id = ?", sArgs);
    }

    public static int delete(DbHelper dbHelper){
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "1",  null);
    }
}
