package com.example.lesbonscomptes.ui.membres;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;

import java.util.ArrayList;
import java.util.List;

public class MembersFragment extends Fragment {

    private MembersViewModel membersViewModel;
    private MembersAdapter arrayAdapter;
    private long groupID = 1; //This param should be passed when opening a group



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        membersViewModel = new ViewModelProvider(this).get(MembersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_members, container, false);
        membersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView membersListView = getView().findViewById(R.id.members_list);
        arrayAdapter.DBHELPER = new DbHelper(getContext());
        arrayAdapter = new MembersAdapter(getContext(), R.layout.member_entry, groupID);
        membersListView.setAdapter(arrayAdapter);

    }





}