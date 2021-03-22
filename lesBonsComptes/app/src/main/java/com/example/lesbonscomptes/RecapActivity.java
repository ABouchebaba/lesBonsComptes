package com.example.lesbonscomptes;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.UFormat;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Participant;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RecapActivity extends AppCompatActivity {

    LinearLayout mRecapListLayout;
    private static DbHelper db;

    long mGroupId;
    List<Member> mGroupMembers;
    List<Expenditure> mGroupExpenditures;
    List<Participant> participantsList;
    List<Due> dueList;
    List<Member> contactList;
    String message = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DbHelper(this);
        mGroupId = Long.parseLong(getIntent().getStringExtra("groupId"));
        mRecapListLayout = findViewById(R.id.recapList);

        mGroupMembers = Member.findByGroupId(db,mGroupId);
        mGroupExpenditures = Expenditure.findByGroupId(db,mGroupId);
        contactList = new ArrayList<Member>();
        ActivityCompat.requestPermissions(RecapActivity.this,new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        calculateDues();

    }


    public void calculateDues(){

        float dueAmount;
        dueList = new ArrayList<Due>();

        for(Member member : mGroupMembers){
            for(Expenditure expenditure : mGroupExpenditures){
                participantsList = Participant.findByExpenditureId(db,expenditure.getId());
                dueAmount = expenditure.getCost()/participantsList.size();

                if(isNotPayer(member,expenditure) && isParticipant(member)){
                    if (isAlreadyInDueList(expenditure.getPayerId())) {
                        increaseDueOfMemberInDuList(expenditure.getPayerId(), dueAmount);
                    }else {

                        dueList.add(new Due(db, expenditure.getPayerId(), dueAmount));
                    }
                }
                if(isPayer(member,expenditure)){
                    for(Participant participant : participantsList) {
                        if (member.getId() != participant.getMemberId()) {
                            if (isAlreadyInDueList(participant.getMemberId())) {
                                decreaseDueOfMemberInDuList(participant.getMemberId(), dueAmount);
                            }else{
                                dueList.add(new Due(db, participant.getMemberId(), dueAmount * -1));
                            }
                        }
                    }
                }
                participantsList.clear();
            }
            addToView(member);
            dueList.clear();
        }
    }

    public void addToView(Member member){
        String s;
        if(HasAdue()){
            Member m = member;
            contactList.add(m);
            LinearLayout ly = new LinearLayout(getApplicationContext());
            TextView tv = new TextView(getApplicationContext());

            tv.setTextSize(20);
            s = "  "+member.getName()+" has to pay back";
            tv.setText(s);
            message = message + s + "\n";

            tv.setWidth(10000);
            tv.setBackgroundResource(R.color.white);
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);

            ly.addView(tv);
            mRecapListLayout.addView(ly);

            for(Due due: dueList){
                if(due.getTotalDue() > 0) {
                    TextView t = new TextView(getApplicationContext());

                    t.setTextSize(16);
                    s = "        "+due.getTotalDue()+" €  to  "+due.getMember().getName();
                    t.setText(s);
                    message = message+ s +"\n";

                    mRecapListLayout.addView(t);
                }
            }
            TextView t = new TextView(getApplicationContext());
            t.setText("      ");
            mRecapListLayout.addView(t);
        }
    }

    public void sendSMS(View v){
        SmsManager sms = SmsManager.getDefault();
        AlertDialog.Builder builder = new AlertDialog.Builder(RecapActivity.this);
        builder.setTitle("Send summary");
        builder.setMessage("Send a summary sms for the group members ? ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<String> multiPartMessage = sms.divideMessage("Group \""+Group.find(db,mGroupId).getName()+"\": \n"+message);
                for(Member m : contactList){
                    sms.sendMultipartTextMessage(m.getPhone(), null,multiPartMessage, null, null);
                }
                Toast.makeText(getApplicationContext()," SMS sent for members",Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean isPayer(Member m, Expenditure e){
        if(e.getPayerId() == m.getId()){
            return true;
        };
        return false;
    }

    public boolean isNotPayer(Member m, Expenditure e){
        if(! isPayer(m,e)){
            return true;
        }
        return  false;
    }

    public boolean isParticipant(Member m){
        for (Participant participant : participantsList){
            if(m.getId() == participant.getMemberId()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAlreadyInDueList (long idMember){
        if(dueList.size()==0){
            return false;
        }
        for (Due due : dueList){
            if(idMember == due.getMember().getId()) {
                return true;
            }
        }
        return false;
    }

    public void increaseDueOfMemberInDuList(long idMember, float dueAmount){
        for (Due due : dueList){
            if(idMember == due.getMember().getId()){
                due.increaseDue(dueAmount);
            }
        }
    }

    public void decreaseDueOfMemberInDuList(long idMember, float dueAmount){
        for (Due due : dueList){
            if(idMember == due.getMember().getId()){
                due.decreaseDue(dueAmount);
            }
        }
    }

    public boolean HasAdue(){
        for (Due d :dueList) if(d.getTotalDue()>0) return true;
        return false;
    }
}