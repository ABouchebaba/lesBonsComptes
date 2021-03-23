package com.example.lesbonscomptes.ui.membres;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.lesbonscomptes.GroupActivity;
import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.RecapActivity;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Group;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.ui.depenses.EditDepenseFragment;

import java.util.ArrayList;
import java.util.List;

public class MembersFragment extends Fragment {

    private MembersViewModel membersViewModel;
    private MembersAdapter arrayAdapter;
    private long groupID = 0; //This param should be passed when opening a group



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        membersViewModel = new ViewModelProvider(this).get(MembersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_members, container, false);
        membersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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

        ListView membersListView = getView().findViewById(R.id.members_list);
        arrayAdapter.DBHELPER = new DbHelper(getContext());
        arrayAdapter = new MembersAdapter(getContext(), R.layout.member_entry, groupID);
        membersListView.setAdapter(arrayAdapter);

        getView().findViewById(R.id.add_member_btn).setOnClickListener(v->{
            DialogFragment newFragment = AddMemberFragment.newInstance(groupID, "");
            newFragment.show(getFragmentManager(), "dialog");
            getFragmentManager().executePendingTransactions();
            newFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    arrayAdapter.updateList();
                }
            });
        });

        getView().findViewById(R.id.recap_btn).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), RecapActivity.class);

            intent.putExtra("groupId",String.valueOf(Group.GROUPID));
            startActivity(intent);
        });

    }





}