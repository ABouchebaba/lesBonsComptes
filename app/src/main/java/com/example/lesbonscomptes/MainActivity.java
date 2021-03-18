package com.example.lesbonscomptes;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Participant;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DbHelper(this);

        initDB(); // must be after init of variable db

        List<Member> members = Member.findByGroupId(db, 1L);
        for (Member m : members){
            System.out.println(m.getName() + " " + m.getId());
        }

        setContentView(R.layout.activity_main);
    }


    private void initDB(){
        // start from scratch

        // Dropping tables
        db.getWritableDatabase().execSQL(Participant.dropQuery());
        db.getWritableDatabase().execSQL(Expenditure.dropQuery());
        db.getWritableDatabase().execSQL(Member.dropQuery());
        db.getWritableDatabase().execSQL(Group.dropQuery());

        // Creating tables again
        db.getWritableDatabase().execSQL(Group.createQuery());
        db.getWritableDatabase().execSQL(Member.createQuery());
        db.getWritableDatabase().execSQL(Expenditure.createQuery());
        db.getWritableDatabase().execSQL(Participant.createQuery());

        // Populate tables
        Group g1 = new Group(null, "G1");
        g1 = g1.save(db);

        Group g2 = new Group(null, "G2");
        g2 = g2.save(db);
        ////////////////////////////////////////////////////

        Member m1 = new Member(null, "m1", "099999999", g1.getId());
        m1 = m1.save(db);

        Member m2 = new Member(null, "m2", "088888888", g1.getId());
        m2 = m2.save(db);

        Member m3 = new Member(null, "m3", "077777777", g1.getId());
        m3 = m3.save(db);
        ///////////////////////////////////////////////////

        Expenditure e1 = new Expenditure(null, 900.f, new Date(), "Tacos", m1.getId(), g1.getId() );
        e1 = e1.save(db);

        Expenditure e2 = new Expenditure(null, 600.f, new Date(), "Pizza", m2.getId(), g1.getId() );
        e2 = e2.save(db);
        ///////////////////////////////////////////////////

        Participant p1 = new Participant(null, e1.getId(), m2.getId());
        p1 = p1.save(db);

        Participant p2 = new Participant(null, e1.getId(), m3.getId());
        p2 = p2.save(db);
    }
}