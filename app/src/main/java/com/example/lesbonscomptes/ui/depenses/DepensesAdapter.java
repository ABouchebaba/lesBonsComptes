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


    public DepensesAdapter(Context context, int layoutResourceId) {
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
        Date date = new Date(2021,4,15);
        List<Expenditure> depenseList = new ArrayList();
        depenseList.add(new Expenditure((long) 1, 100,   date, (long) 1));
        depenseList.add(new Expenditure((long) 2, 200,   date, (long) 2));
        depenseList.add(new Expenditure((long) 3, 136,   date, (long) 1));
        depenseList.add(new Expenditure((long) 4, 215,   date, (long) 2));
        depenseList.add(new Expenditure((long) 5, 81,   date, (long) 1));
        depenseList.add(new Expenditure((long) 6, 20,   date, (long) 2));
        depenseList.add(new Expenditure((long) 7, 36,   date, (long) 1));
        depenseList.add(new Expenditure((long) 8, 30,   date, (long) 1));
        ArrayList<String[]> list = new ArrayList();

//        depenseList = Expenditure.find(DBHELPER);

        for(Expenditure depense : depenseList){
            Member payer = Member.find(DBHELPER, depense.getPayerId());
            list.add( new String[] {depense.getId().toString(), "TITLE", payer.getName(), depense.getCost()+""});
            depense.save(DBHELPER);
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