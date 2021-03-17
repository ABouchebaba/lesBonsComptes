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

        // start from scratch
        Participant.delete(db);
        Expenditure.delete(db);
        Member.delete(db);
        Group.delete(db);
        //////////////////////////////////////////////////////////

        Group g1 = new Group(1L, "G1");
        g1.save(db); // insert in db

        Group g2 = new Group(2L, "G2");
        g2.save(db); // insert in db

        /////////////////////////////////////////////////////////////////////////

        Member m1 = new Member(1L, "amine", "099999999", g1.getId());
        m1.save(db);

        Member m2 = new Member(2L, "lol", "099999998", g2.getId());
        m2.save(db);

        Member m3 = new Member(null, "amine", "099999999", g1.getId());
        m3.save(db);

//        Expenditure e = new Expenditure(1L, 20.0f, new Date(), m1.getId());
//        e.save(db);
//
//        Expenditure tmp = Expenditure.find(db, 1L);
//        System.out.println(tmp.getDate().toString() + " -- " + tmp.getCost());
        /////////////////////////////////////////////////////////////////////////

        // this should also delete m1, m2 and e from db
//        Group.delete(db, group.getId());
//        m1 = Member.find(db, 1L);
//        m2 = Member.find(db, 2L);
//        e = Expenditure.find(db, 1L);

//        System.out.println(Member.find(db).size());

//        System.out.println(m1 == null && m2 == null && e == null);

        /////////////////////////////////////////////////////////////////////////

        List<Member> members = Member.findByGroupId(db, g1.getId());
        for (Member m : members){
            System.out.println(m.getName() + " " + m.getId());
        }

        setContentView(R.layout.activity_main);
    }
}