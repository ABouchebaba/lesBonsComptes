package com.example.lesbonscomptes.ui.depenses;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DepensesAdapter extends ArrayAdapter {


    private final Context context;
    private final Fragment fragment;
    private ArrayList<String[]> data;
    private final int layoutResourceId;
    public static DbHelper DBHELPER;
    private static long GROUPID;

    public DepensesAdapter(Context context, int layoutResourceId, long groupID, Fragment fragment) {
        super(context, layoutResourceId, loadData());
        this.fragment=fragment;
        GROUPID = groupID;
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
            holder.title = (TextView)row.findViewById(R.id.depense_title);
            holder.payer = (TextView)row.findViewById(R.id.depense_payer);
            holder.cost = (TextView)row.findViewById(R.id.depense_cost);

            row.setTag(holder);

            row.findViewById(R.id.detail_depense_btn).setOnClickListener(this::detail_depense);

            ImageButton delete_depense_btn = row.findViewById(R.id.delete_depense_btn);

            delete_depense_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Confimation");

                    builder.setMessage("Supprimer la dépense ?");

                    Long depense_id = Long.parseLong(((View) delete_depense_btn.getParent()).findViewById(R.id.depense_title).getTag().toString());

                    builder.setPositiveButton("Oui",(dialog, which) -> {
                        delete_depense(depense_id);
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
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        String[] tache = data.get(position);

        holder.title.setText(tache[1]);
        holder.title.setTag(tache[0]);
        holder.payer.setText(tache[2]);
        holder.cost.setText(tache[3]);

        return row;
    }



    static class ViewHolder
    {
        TextView title;
        TextView payer;
        TextView cost;

    }

    private static ArrayList<String[]> loadData(){
        List<Expenditure> depenseList = Expenditure.findByGroupId(DBHELPER, GROUPID);
        ArrayList<String[]> list = new ArrayList();
        for(Expenditure depense : depenseList){
            Member payer = Member.find(DBHELPER, depense.getPayerId());
            list.add( new String[] {depense.getId().toString(), depense.getTitle(), payer.getName(), depense.getCost()+""});
        }
        return  list;
    }

    public void updateList(){

        ArrayList<String[]> depenses = loadData();
        this.data = depenses;
        this.clear();
        this.addAll(depenses);
        this.notifyDataSetChanged();
    }

    private void detail_depense(View v) {
        Long depense_id = Long.parseLong(((View) v.getParent()).findViewById(R.id.depense_title).getTag().toString());
        Expenditure depense = Expenditure.find(DBHELPER, depense_id);
        edit_depense(depense);
    }

    public void delete_depense(long depense_id){
        Expenditure.delete(DBHELPER, depense_id);
        this.updateList();
    }

    public void edit_depense(Expenditure depense) {
        List<Member> membersList = Member.findByGroupId(this.DBHELPER, GROUPID);
        ArrayList<String> membersNames = new ArrayList();
        ArrayList<Long> membersIds = new ArrayList();
        for(Member member : membersList){
            membersNames.add( member.getName());
            membersIds.add( member.getId());
        }
        long[] ids = new long[membersIds.size()];
        int index = 0;
        for (final Long value : membersIds) {
            ids[index++] = value;
        }
        DialogFragment newFragment = EditDepenseFragment.newInstance(membersNames, ids, GROUPID, depense.getId());
        newFragment.show(this.fragment.getFragmentManager(), "dialog");
    }





}