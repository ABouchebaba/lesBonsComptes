package com.example.lesbonscomptes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.DateTimePatternGenerator;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Participant;

import org.w3c.dom.Text;

import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static DbHelper db;
    LinearLayout mGroupsListLayout;
    List<Group> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DbHelper(this);
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
                    /*
                    Intent i = new Intent(this, .class);
                    */
                    Toast.makeText(getApplicationContext(),g.getName(),Toast.LENGTH_SHORT).show();
                    /*
                    i.putExtra("MyClass", (Parcelable) g);
                    startActivity(i);

                    */
                }
            });

            group.addView(groupName);
            group.addView(delete);
            mGroupsListLayout.addView(group);
        }
    }
}