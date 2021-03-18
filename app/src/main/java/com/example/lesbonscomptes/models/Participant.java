package com.example.lesbonscomptes.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lesbonscomptes.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class Participant {

    private final static String TABLE_NAME = "participants";

    private Long id;
    private Long expenditureId;
    private Long memberId;

    public Participant() {
    }

    public Participant(Long id, Long expenditureId, Long memberId) {
        this.id = id;
        this.expenditureId = expenditureId;
        this.memberId = memberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExpenditureId() {
        return expenditureId;
    }

    public void setExpenditureId(Long expenditureId) {
        this.expenditureId = expenditureId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public static String createQuery(){
        return
                "CREATE TABLE "+ TABLE_NAME +" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "expenditure_id" + " INTEGER NOT NULL," +
                "member_id INTEGER NOT NULL," +
                "FOREIGN KEY (expenditure_id) references expenditures(id) ON DELETE CASCADE," +
                "FOREIGN KEY (member_id) references members(id) ON DELETE CASCADE)";
    }

    public static String dropQuery(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public void save(DbHelper db){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("expenditure_id", expenditureId);
        contentValues.put("member_id", memberId);

        db.getWritableDatabase()
                .insertWithOnConflict(
                        TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);

    }

    public static Participant find(DbHelper db, Long id){
        String[] columns = new String[]{"id", "expenditure_id", "member_id"};
        String[] sArgs = new String[]{""+id};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "id = ?", sArgs, null,null,null);

        // not found
        if (cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
        Long tmp_expenditure_id = cursor.getLong(cursor.getColumnIndex("expenditure_id"));
        Long tmp_member_id = cursor.getLong(cursor.getColumnIndex("member_id"));

        cursor.close();

        return new Participant(tmp_id, tmp_expenditure_id, tmp_member_id);
    }

    public static List<Participant> find(DbHelper db){
        String[] columns = new String[]{"id", "expenditure_id", "member_id"};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, null, null, null,null,null);

        List<Participant> participants = new ArrayList<>();

        while (cursor.moveToNext()){
            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
            Long tmp_expenditure_id = cursor.getLong(cursor.getColumnIndex("expenditure_id"));
            Long tmp_member_id = cursor.getLong(cursor.getColumnIndex("member_id"));

            participants.add(new Participant(tmp_id, tmp_expenditure_id, tmp_member_id));
        }
        cursor.close();

        return participants;
    }

    public static List<Member> findMembersByExpenditureId(DbHelper db, Long expenditureId){
        String[] columns = new String[]{"id", "expenditure_id", "member_id"};
        String[] sArgs = new String[]{""+expenditureId};

        Cursor cursor = db.getReadableDatabase()
                .query(TABLE_NAME, columns, "expenditure_id = ?", sArgs, null,null,null);

        List<Member> participants = new ArrayList<>();

        while (cursor.moveToNext()){
//            Long tmp_id = cursor.getLong(cursor.getColumnIndex("id"));
//            Long tmp_expenditure_id = cursor.getLong(cursor.getColumnIndex("expenditure_id"));
            Long tmp_member_id = cursor.getLong(cursor.getColumnIndex("member_id"));

            participants.add(Member.find(db, tmp_member_id));
        }
        cursor.close();

        return participants;
    }

    public static int delete(DbHelper dbHelper, Long id){
        String[] sArgs = new String[]{""+id};
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "id = ?", sArgs);
    }

    public static int delete(DbHelper dbHelper){
        return dbHelper.getWritableDatabase().delete(TABLE_NAME, "1",  null);
    }


}
