package com.example.lesbonscomptes;

import android.os.Bundle;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Participant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    public static DbHelper DBHELPER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHELPER = new DbHelper(this);

        //DB
       initDB();
        setContentView(R.layout.activity_group);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_depenses, R.id.navigation_members)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    private void initDB() {
        List<Member> membersList = new ArrayList();
        new Group((long)1, "G1").save(DBHELPER);
        new Group((long)2, "G2").save(DBHELPER);
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
        participantList.add(new Participant(null,(long)1,(long)1));
        participantList.add(new Participant(null,(long)1,(long)3));
        participantList.add(new Participant(null,(long)1,(long)4));
        participantList.add(new Participant(null,(long)1,(long)5));
        participantList.add(new Participant(null,(long)2,(long)1));
        participantList.add(new Participant(null,(long)2,(long)3));
        participantList.add(new Participant(null,(long)2,(long)4));
        participantList.add(new Participant(null,(long)2,(long)5));
        participantList.add(new Participant(null,(long)3,(long)9));
        participantList.add(new Participant(null,(long)2,(long)10));
        for(Participant p : participantList){
            p.save(DBHELPER);
        }




    }


}