package com.example.lesbonscomptes;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Member;

import java.util.List;

public class RecapActivity extends AppCompatActivity {

    private static DbHelper db;
    long mGroupId;
    LinearLayout mRecapList;
    List<Member> mGroupMembers;
    List<Expenditure> mGroupExpenditures;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DbHelper(this);
        mGroupId = Long.parseLong(getIntent().getStringExtra("groupId"));
        mRecapList = findViewById(R.id.recapList);

        mGroupMembers = Member.findByGroupId(db,mGroupId);
        mGroupExpenditures = Expenditure.findByGroupId(db,mGroupId);

        for(Member member : mGroupMembers){

        }

    }

    public void justToSee(View v){
        Toast.makeText(getApplicationContext(),String.valueOf(mGroupId),Toast.LENGTH_SHORT).show();
    }
}