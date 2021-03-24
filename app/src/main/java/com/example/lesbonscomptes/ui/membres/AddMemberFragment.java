package com.example.lesbonscomptes.ui.membres;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Member;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMemberFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AddMemberFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private long groupID;
    private String mParam2;
    private DbHelper DBHELPER;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMemberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMemberFragment newInstance(long param1, String param2) {
        AddMemberFragment fragment = new AddMemberFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddMemberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            groupID = getArguments().getLong(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.DBHELPER = new DbHelper(getContext());

        getView().findViewById(R.id.save_member_btn).setOnClickListener(v->{
            EditText nomET = getView().findViewById(R.id.nomET);
            EditText phoneET = getView().findViewById(R.id.phoneET);
            String nom = nomET.getText().toString();
            String phone = phoneET.getText().toString();
            Member member = new Member(null, nom, phone, groupID);
            member.save(DBHELPER);
            getDialog().dismiss();
        });

        //Cancel Button
        getView().findViewById(R.id.cancel_member_btn).setOnClickListener(v->{
            getDialog().cancel();
        });

    }
}