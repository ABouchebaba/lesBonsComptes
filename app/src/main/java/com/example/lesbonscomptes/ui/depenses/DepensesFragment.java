package com.example.lesbonscomptes.ui.depenses;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.RecapActivity;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;

import java.util.ArrayList;
import java.util.List;

public class DepensesFragment extends Fragment {

    private DepensesViewModel depensesViewModel;
    public DepensesAdapter arrayAdapter;
    private long groupID = 0; //This param should be passed when opening a group
    private ViewGroup container;


    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        this.container = container;
        depensesViewModel =                new ViewModelProvider(this).get(DepensesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_depenses, container, false);
        depensesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        groupID = Group.GROUPID;
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView membersListView = getView().findViewById(R.id.depenses_list);
        arrayAdapter.DBHELPER = new DbHelper(getContext());
        arrayAdapter = new DepensesAdapter(getContext(), R.layout.depense_entry, groupID, this);
        membersListView.setAdapter(arrayAdapter);

        getView().findViewById(R.id.add_depense_btn).setOnClickListener(v -> {
            new_depense();
        });

        ListView depenseList = getView().findViewById(R.id.depenses_list);
        depenseList.setOnItemClickListener((parent, view1, position, id) -> {
            Long depense_id = Long.parseLong(((View) getView().getParent()).findViewById(R.id.depense_title).getTag().toString());
            Expenditure depense = Expenditure.find(arrayAdapter.DBHELPER, depense_id);
            edit_depense(depense);
        });

        getView().findViewById(R.id.recap_btn).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RecapActivity.class);

            intent.putExtra("groupId",String.valueOf(Group.GROUPID));
            startActivity(intent);
        });

    }



    @Override
    public void onResume() {
        super.onResume();
        arrayAdapter.updateList();
    }

    private void new_depense()
    {
        List<Member> membersList = Member.findByGroupId(arrayAdapter.DBHELPER, groupID);
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
        DialogFragment newFragment = EditDepenseFragment.newInstance(membersNames, ids, groupID, 0);
        newFragment.show(getFragmentManager(), "dialog");
        getFragmentManager().executePendingTransactions();
        newFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                arrayAdapter.updateList();
            }
        });
    }

    public void edit_depense(Expenditure depense) {
        List<Member> membersList = Member.findByGroupId(arrayAdapter.DBHELPER, groupID);
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
        DialogFragment newFragment = EditDepenseFragment.newInstance(membersNames, ids, groupID, depense.getId());
        newFragment.show(getFragmentManager(), "dialog");
    }

}