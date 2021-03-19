package com.example.lesbonscomptes.ui.membres;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;

import java.util.ArrayList;
import java.util.List;

public class MembersAdapter extends ArrayAdapter {


    private final Context context;
    private final ArrayList<String[]> data;
    private final int layoutResourceId;
    public static DbHelper DBHELPER;


    public MembersAdapter(Context context, int layoutResourceId) {
        super(context, layoutResourceId, loadData());
        this.context = context;
        this.data = loadData();
        this.layoutResourceId = layoutResourceId;
        this.updateList();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView)row.findViewById(R.id.member_name);
            holder.phone = (TextView)row.findViewById(R.id.member_phone);

            row.setTag(holder);

            row.findViewById(R.id.delete_member_btn).setOnClickListener(this::delete_member);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        String[] tache = data.get(position);

        holder.name.setText(tache[1]);
        holder.name.setTag(tache[0]);
        holder.phone.setText(tache[2]);

        return row;
    }

    static class ViewHolder
    {
        TextView name;
        TextView phone;

    }

    private static ArrayList<String[]> loadData(){

        List<Member> membersList = new ArrayList();
        new Group((long)1, "G1").save(DBHELPER);
        new Group((long)2, "G2").save(DBHELPER);
        membersList.add(new Member((long) 1, "Chems",   "0766685331", (long) 1));
        membersList.add(new Member((long) 2, "Sofiane", "0678953100", (long) 1));
        membersList.add(new Member((long) 3, "Amine",   "0681100237", (long) 1));
        membersList.add(new Member((long) 4, "Salim",   "0699190159", (long) 1));
        membersList.add(new Member((long) 5, "Noura",   "0659100635", (long) 1));
        membersList.add(new Member((long) 6, "Samir",   "0660541935", (long) 2));
        membersList.add(new Member((long) 7, "Asma",    "0689100980", (long) 1));
        membersList.add(new Member((long) 8, "Asia",    "0689100980", (long) 1));
        membersList.add(new Member((long) 9, "Kamel",    "0689100980", (long) 1));
        membersList.add(new Member((long) 10, "Wahid",    "0689100980", (long) 1));
        ArrayList<String[]> list = new ArrayList();

//        membersList = Member.find(DBHELPER);

        for(Member member : membersList){
            list.add( new String[] {member.getId().toString(), member.getName(), member.getPhone()});
            member.save(DBHELPER);
        }

        return  list;

    }

    public void updateList(){

        List<String[]> members = loadData();
        this.clear();
        this.addAll(members);
        this.notifyDataSetChanged();
    }



    public void delete_member(View v){
        Long member_id = Long.parseLong(((View) v.getParent()).findViewById(R.id.member_name).getTag().toString());
        Member.delete(DBHELPER, member_id);
        this.updateList();
        this.updateList();
    }






}