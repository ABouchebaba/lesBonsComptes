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
    private String title;
    private Long payerId;
    private Long groupId;

    public Expenditure(Long id, float cost, Date date, String title, Long payerId, Long groupId) {
        this.id = id;
        this.cost = cost;
        this.date = date;
        this.title = title;
        this.payerId = payerId;
        this.groupId = groupId;
    }

    public Expenditure(Long id, float cost, Date date, Long payerId, Long groupId) {
        this.id = id;
        this.cost = cost;
        this.date = date;
        this.title = "";
        this.payerId = payerId;
        this.groupId = groupId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
                    "cost REAL NOT NULL," +
                    "title TEXT NOT NULL," +
                    "payer_id" + " INTEGER NOT NULL," +
                    "group_id" + " INTEGER NOT NULL," +
                    "date TEXT NOT NULL," +
                    "FOREIGN KEY (payer_id) references members(id) ON DELETE CASCADE,"+
                    "FOREIGN KEY (group_id) references groups(id) ON DELETE CASCADE)";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public Expenditure save(DbHelper db){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("cost", cost);
        contentValues.put("title", title);
        contentValues.put("payer_id", payerId);
        contentValues.put("group_id", groupId);
        contentValues.put("date", date.toString());

        db.getWritableDatabase()
                .insertWithOnConflict(
                        TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);

        return getLastInserted(db);
    }

    private Expenditure getLastInserted(DbHelper db){

        Cursor cursor = db.getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE id = last_insert_rowid();", null);

        // not found
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
        float tmp_cost = cursor.getFloat(cursor.getColumnIndex("cost"));
        String tmp_title = cursor.getString(cursor.getColumnIndex("title"));
        Long tmp_payer_id = cursor.getLong(cursor.getColumnIndex("payer_id"));
        Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));
        String tmp_date = cursor.getString(cursor.getColumnIndex("date"));

        cursor.close();
        return new Expenditure(tmp_id, tmp_cost, new Date(tmp_date), tmp_title , tmp_payer_id, tmp_group_id);
    }

    public List<Member> listParticipants(DbHelper db){

        List<Participant> participants = Participant.findByExpenditureId(db, id);
        List<Member> members = new ArrayList<>();

        for (Participant p : participants){
            members.add(Member.find(db, p.getMemberId()));
        }

        return members;
    }

    public static Expenditure find(DbHelper db, Long id){
        String[] columns = new String[]{"id", "title", "cost", "payer_id", "group_id", "date"};
        String[] sArgs = new String[]{""+id};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "id = ?", sArgs, null,null,null);

        // not found
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
        float tmp_cost = cursor.getFloat(cursor.getColumnIndex("cost"));
        String tmp_title = cursor.getString(cursor.getColumnIndex("title"));
        Long tmp_payer_id = cursor.getLong(cursor.getColumnIndex("payer_id"));
        Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));
        String tmp_date = cursor.getString(cursor.getColumnIndex("date"));

        cursor.close();
        return new Expenditure(tmp_id, tmp_cost, new Date(tmp_date), tmp_title , tmp_payer_id, tmp_group_id);
    }

    public static List<Expenditure> find(DbHelper db){
        String[] columns = new String[]{"id", "title", "cost", "payer_id", "group_id", "date"};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, null, null, null,null,null);

        List<Expenditure> expenditures = new ArrayList<>();

        while (cursor.moveToNext()){
            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
            float tmp_cost = cursor.getFloat(cursor.getColumnIndex("cost"));
            String tmp_title = cursor.getString(cursor.getColumnIndex("title"));
            Long tmp_payer_id = cursor.getLong(cursor.getColumnIndex("payer_id"));
            Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));
            String tmp_date = cursor.getString(cursor.getColumnIndex("date"));

            expenditures.add(new Expenditure(tmp_id, tmp_cost, new Date(tmp_date), tmp_title , tmp_payer_id, tmp_group_id));
        }
        cursor.close();

        return expenditures;
    }

    public static List<Expenditure> findByGroupId(DbHelper db, Long groupId){
        String[] columns = new String[]{"id", "title", "cost", "payer_id", "group_id", "date"};
        String[] sArgs = new String[]{""+groupId};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "group_id = ?", sArgs, null,null,null);

        List<Expenditure> expenditures = new ArrayList<>();

        while (cursor.moveToNext()){
            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
            float tmp_cost = cursor.getFloat(cursor.getColumnIndex("cost"));
            String tmp_title = cursor.getString(cursor.getColumnIndex("title"));
            Long tmp_payer_id = cursor.getLong(cursor.getColumnIndex("payer_id"));
            Long tmp_group_id = cursor.getLong(cursor.getColumnIndex("group_id"));
            String tmp_date = cursor.getString(cursor.getColumnIndex("date"));

            expenditures.add(new Expenditure(tmp_id, tmp_cost, new Date(tmp_date), tmp_title , tmp_payer_id, tmp_group_id));
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
