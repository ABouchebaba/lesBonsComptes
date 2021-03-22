package com.example.lesbonscomptes.ui.depenses;

import android.app.Dialog;
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
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Member;

import java.util.ArrayList;
import java.util.List;

public class DepensesFragment extends Fragment {

    private DepensesViewModel depensesViewModel;
    private DepensesAdapter arrayAdapter;
    private long groupID = 1; //This param should be passed when opening a group
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
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView membersListView = getView().findViewById(R.id.depenses_list);
        arrayAdapter.DBHELPER = new DbHelper(getContext());
        arrayAdapter = new DepensesAdapter(getContext(), R.layout.depense_entry, groupID);
        membersListView.setAdapter(arrayAdapter);

        getView().findViewById(R.id.add_depense_btn).setOnClickListener(v -> {
            edit_new_depense();
        });

    }

    private void edit_new_depense()
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
        DialogFragment newFragment = EditDepenseFragment.newInstance(membersNames, ids);
        newFragment.show(getFragmentManager(), "dialog");



    }

}