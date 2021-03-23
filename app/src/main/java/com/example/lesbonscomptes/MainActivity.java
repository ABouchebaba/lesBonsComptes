package com.example.lesbonscomptes;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Participant;

import java.util.ArrayList;
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
        dialog.setTitle("Ajouter un groupe");
        dialog.setMessage("Nom du groupe :");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        dialog.setView(input);

        dialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
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
        }).setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @SuppressLint("ResourceAsColor")
    public void refreshView(){

        mGroupsListLayout.removeAllViews();
        mList = Group.find(db);

        for(Group g : mList){

            LinearLayout group = new LinearLayout(getApplicationContext());
            group.setTag(g);
            group.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            group.setMinimumHeight(LinearLayout.LayoutParams.MATCH_PARENT);
            group.setPadding(100,5,5,50);
            group.setOrientation(LinearLayout.HORIZONTAL);


            Button delete = new Button(getApplicationContext());

            delete.setText("Delete");
            delete.setTextColor(Color.WHITE);
            delete.setBackgroundColor(R.color.red);
            delete.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);


            TextView groupName = new TextView(getApplicationContext());
            groupName.setTextColor(Color.BLACK);
            groupName.setTextSize(20);
            groupName.setPadding(100,0,0,0);
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

                    builder.setMessage("Supprimer le groupe : "+g.getName()+" ?");

                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Group.delete(db,g.getId());
                            refreshView();
                        }
                    });

                    // Set the alert dialog no button click listener
                    builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
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
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            ////Afficher les detail d'un group INTENT /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Group g = (Group) group.getTag();

                    Intent intent = new Intent(getApplicationContext(), GroupActivity.class);

                    intent.putExtra("groupId",String.valueOf(g.getId()));
                    startActivity(intent);

                }
            });

            group.addView(delete);
            group.addView(groupName);
            mGroupsListLayout.addView(group);
        }
    }


    private void initDB() {
        DbHelper DBHELPER = db;
        List<Member> membersList = new ArrayList();
        new Group((long)1, "Sortie Plage").save(DBHELPER);
        new Group((long)2, "Travaux Garage").save(DBHELPER);
        membersList.add(new Member((long) 1, "Chems",   "0766685331", (long) 1));
        membersList.add(new Member((long) 2, "Sofiane", "0678953100", (long) 2));
        membersList.add(new Member((long) 3, "Amine",   "0681100237", (long) 1));
        membersList.add(new Member((long) 4, "Salim",   "0699190159", (long) 1));
        membersList.add(new Member((long) 5, "Noura",   "0659100635", (long) 1));
        membersList.add(new Member((long) 6, "Samir",   "0660541935", (long) 2));
        membersList.add(new Member((long) 7, "Asma",    "0689100980", (long) 1));
        membersList.add(new Member((long) 8, "Asia",    "0689100980", (long) 1));
        membersList.add(new Member((long) 9, "Kamel",    "0689100980", (long) 1));
        membersList.add(new Member((long) 10, "Wahid",    "0689100980", (long) 1));
        for(Member member : membersList){
            member.save(DBHELPER);
        }
        Date date = new Date(2021,4,15);
        List<Expenditure> depenseList = new ArrayList();
        depenseList.add(new Expenditure((long) 1, (float) 7.2,   date, "Boisson",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 2, (float) 170,   date, "Sushi",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 3, (float) 15.84,   date, "Transport",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 4, (float) 20.74,   date, "Musée",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 5, (float) 15.3,   date, "Tacos",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 6, (float) 5.45,   date, "Chips",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 7, (float) 4.22 ,  date, "Zoo",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 8, (float) 18.7,   date, "Attraction",(long) 1, (long)1));
        depenseList.add(new Expenditure((long) 9, (float) 3.4,   date, "Autres",(long) 1, (long)2));
        for(Expenditure depense : depenseList){
            depense.save(DBHELPER);
        }
        List<Participant> participantList = new ArrayList();
        participantList.add(new Participant((long)1,(long)1,(long)1));
        participantList.add(new Participant((long)2,(long)1,(long)3));
        participantList.add(new Participant((long)3,(long)1,(long)4));
        participantList.add(new Participant((long)4,(long)1,(long)5));
        participantList.add(new Participant((long)5,(long)2,(long)1));
        participantList.add(new Participant((long)6,(long)2,(long)3));
        participantList.add(new Participant((long)7,(long)2,(long)4));
        participantList.add(new Participant((long)8,(long)2,(long)5));
        participantList.add(new Participant((long)9,(long)3,(long)9));
        participantList.add(new Participant((long)10,(long)2,(long)10));
        for(Participant p : participantList){
            p.save(DBHELPER);
        }
    }


}