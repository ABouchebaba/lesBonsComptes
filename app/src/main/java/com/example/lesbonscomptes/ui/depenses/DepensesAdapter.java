package com.example.lesbonscomptes.ui.depenses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DepensesAdapter extends ArrayAdapter {


    private final Context context;
    private final ArrayList<String[]> data;
    private final int layoutResourceId;
    public static DbHelper DBHELPER;
    private static long GROUPID;

    public DepensesAdapter(Context context, int layoutResourceId, long groupID) {
        super(context, layoutResourceId, loadData());
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

            row.findViewById(R.id.delete_depense_btn).setOnClickListener(this::delete_depense);
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

        List<String[]> depenses = loadData();
        this.clear();
        this.addAll(depenses);
        this.notifyDataSetChanged();
    }



    public void delete_depense(View v){
        Long depense_id = Long.parseLong(((View) v.getParent()).findViewById(R.id.depense_title).getTag().toString());
        Expenditure.delete(DBHELPER, depense_id);
        this.updateList();
        Log.d("DELETE DEPENSE", "delete_depense: "+depense_id);
    }






}