package com.example.lesbonscomptes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.telephony.SmsManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Participant;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static DbHelper db;
    LinearLayout mGroupsListLayout;
    List<Group> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);
        initDB();

        mGroupsListLayout = findViewById(R.id.groupsList);
        refreshView();
    }

    public  void addGroup(View v){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add group");
        dialog.setMessage("Enter group name :");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(input);

        dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /////

                long id;
                if (mList.size() == 0){
                    id = 1;
                }else {
                    id = mList.get(mList.size()-1).getId() + 1;
                }

                String s;
                if(input.getText().toString().isEmpty() || input.getText().toString().trim().isEmpty()){
                    s = "Group " + id;
                }else s = input.getText().toString();

                Group g = new Group (id,s);
                g.save(db);
                mList = Group.find(db);
                refreshView();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void refreshView(){

        mGroupsListLayout.removeAllViews();
        mList = Group.find(db);

        for(Group g : mList){

            LinearLayout group = new LinearLayout(getApplicationContext());
            group.setTag(g);
            group.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            group.setMinimumHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            group.setPadding(100,5,5,5);
            group.setOrientation(LinearLayout.HORIZONTAL);


            Button delete = new Button(getApplicationContext());

            delete.setText("Delete");
            delete.setTextColor(Color.WHITE);
            delete.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);



            TextView groupName = new TextView(getApplicationContext());
            groupName.setTextColor(Color.BLACK);
            groupName.setTextSize(20);
            groupName.setGravity(Gravity.CENTER_HORIZONTAL);
            groupName.setText(g.getName());

            /////Supprimer un group
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LinearLayout l = (LinearLayout) delete.getParent();
                    Group g = (Group) l.getTag();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Confimation");

                    builder.setMessage("Delete group : "+g.getName()+" ?");

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Group.delete(db,g.getId());
                            refreshView();
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    // Display the alert dialog on interface
                    dialog.show();
                }
            });

            ////Afficher les detail d'un group INTENT /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Group g = (Group) group.getTag();

                    Intent intent = new Intent(getApplicationContext(), RecapActivity.class);

                    intent.putExtra("groupId",String.valueOf(g.getId()));
                    startActivity(intent);

                }
            });

            group.addView(groupName);
            group.addView(delete);
            mGroupsListLayout.addView(group);
        }
    }

    private void initDB() {
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
        /*
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

        Expenditure e1 = new Expenditure(null, 900.f, new Date(), "Tacos", m1.getId(), g1.getId());
        e1 = e1.save(db);

        Expenditure e2 = new Expenditure(null, 600.f, new Date(), "Pizza", m2.getId(), g1.getId());
        e2 = e2.save(db);
        ///////////////////////////////////////////////////

        Participant p1 = new Participant(null, e1.getId(), m2.getId());
        p1 = p1.save(db);

        Participant p2 = new Participant(null, e1.getId(), m3.getId());
        p2 = p2.save(db);

        */
        Group g1 = new Group();
        g1.setId((long) 1);
        g1.setName("Saturday's party");
        g1.save(db);

        Member m1 = new Member((long) 1,"Amine","+33000000000",g1.getId());
        m1.save(db);
        Member m2 = new Member((long) 2,"Sofiane","+33753069507",g1.getId());
        m2.save(db);
        Member m3 = new Member((long) 3,"Chems","+33111111111",g1.getId());
        m3.save(db);
        Member m4 = new Member((long) 4,"Kamar","+33222222222",g1.getId());
        m4.save(db);


        Date d = new Date();

        Expenditure e1 = new Expenditure((long) 1,(float) 300,d,"E1",m1.getId(),g1.getId());
        e1.save(db);

        Participant p1 = new Participant((long) 1,e1.getId(),m1.getId());
        p1.save(db);
        Participant p2 = new Participant((long) 2,e1.getId(),m2.getId());
        p2.save(db);
        Participant p3 = new Participant((long) 3,e1.getId(),m3.getId());
        p3.save(db);



        Expenditure e2 = new Expenditure((long) 2,(float) 250,d,"E1",m1.getId(),g1.getId());
        e2.save(db);

        Participant p4 = new Participant((long) 4,e2.getId(),m2.getId());
        p4.save(db);
        Participant p5 = new Participant((long) 5,e2.getId(),m3.getId());
        p5.save(db);



        Expenditure e3 = new Expenditure((long) 3,(float) 420,d,"E1",m1.getId(),g1.getId());
        e3.save(db);

        Participant p6 = new Participant((long) 6,e3.getId(),m4.getId());
        p6.save(db);



        Expenditure e4= new Expenditure((long) 4,(float) 500,d,"E4",m4.getId(),g1.getId());
        e4.save(db);

        Participant p7 = new Participant((long) 7,e4.getId(),m1.getId());
        p7.save(db);
        Participant p8 = new Participant((long) 8,e4.getId(),m4.getId());
        p8.save(db);

        Expenditure e5= new Expenditure((long) 5,(float) 1000,d,"E5",m3.getId(),g1.getId());
        e5.save(db);

        Participant p9 = new Participant((long) 9,e5.getId(),m1.getId());
        p9.save(db);
        Participant p10 = new Participant((long) 10,e5.getId(),m3.getId());
        p10.save(db);

    }
}