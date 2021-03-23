package com.example.lesbonscomptes.ui.depenses;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.lesbonscomptes.R;
import com.example.lesbonscomptes.db.DbHelper;
import com.example.lesbonscomptes.models.Expenditure;
import com.example.lesbonscomptes.models.Member;
import com.example.lesbonscomptes.models.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDepenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDepenseFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private  DbHelper DBHELPER;

    // TODO: Rename and change types of parameters
    private ArrayList<String> membersNames;
    private long[] membersIDs;
    private long groupId;
    private long depenseId;

    public EditDepenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param param3 Parameter 3.
     * @param param4 Parameter 4.
     * @return A new instance of fragment EditDepenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDepenseFragment newInstance(ArrayList<String> param1, long[] param2, long param3, long param4) {
        EditDepenseFragment fragment = new EditDepenseFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putLongArray(ARG_PARAM2, param2);
        args.putLong(ARG_PARAM3, param3);
        args.putLong(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            membersNames = getArguments().getStringArrayList(ARG_PARAM1);
            membersIDs = getArguments().getLongArray(ARG_PARAM2);
            groupId = getArguments().getLong(ARG_PARAM3);
            depenseId = getArguments().getLong(ARG_PARAM4);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_edit_depense, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.DBHELPER = new DbHelper(getContext());
        //Spinner of payer
        ArrayList<String> arraySpinner = this.membersNames;
        Spinner payeurSpinner = (Spinner) getView().findViewById(R.id.payeur_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payeurSpinner.setAdapter(adapter);

        //List of participants
        ListView participantsListView = getView().findViewById(R.id.participantsListView);
        participantsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        participantsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
            }
        });
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_checked , membersNames);
        participantsListView.setAdapter(arrayAdapter);

        //Save Button
        getView().findViewById(R.id.save_depense_btn).setOnClickListener(v -> {
            long payeurId = this.membersIDs[payeurSpinner.getSelectedItemPosition()];
            EditText titleET = getView().findViewById(R.id.titleET);
            EditText sommeET = getView().findViewById(R.id.sommeET);
            EditText dateET = getView().findViewById(R.id.dateET);

            float somme = Float.parseFloat(sommeET.getText().toString());
            String title = titleET.getText().toString();
//            Date date = new Date(dateET.getYear(), dateET.getMonth(), dateET.getDayOfMonth());
            Expenditure depense;
            depense = new Expenditure(null, somme, new Date(2021,4,1), title, payeurId, groupId);
            depense = depense.save(DBHELPER);

            //Participants
            SparseBooleanArray participantsChecked = participantsListView.getCheckedItemPositions();
            ArrayList<Long> participantsIds = new ArrayList();
            int i=0;
            while (i<this.membersIDs.length){
                if(participantsChecked.get(i)) {
                    Participant p = new Participant(null, depense.getId(), membersIDs[i]);
                    p.save(DBHELPER);
                }
                i++;
            }
            getDialog().dismiss();
        });

        //Cancel Button
        getView().findViewById(R.id.cancel_btn).setOnClickListener(v->{
            getDialog().dismiss();
        });

        //depenseId !=0 (not creating)
        if(depenseId!=0){
            Expenditure depense = Expenditure.find(DBHELPER, depenseId);
            EditText titleET = getView().findViewById(R.id.titleET);
            EditText sommeET = getView().findViewById(R.id.sommeET);
            EditText dateET = getView().findViewById(R.id.dateET);
            titleET.setText(depense.getTitle());
            sommeET.setText(""+depense.getCost());
            dateET.setText(""+depense.getDate().getDay()+"/"+depense.getDate().getMonth()+"/"+depense.getDate().getYear()+"");
            int payeurPosition = getPosition(membersIDs, depense.getPayerId());
            payeurSpinner.setSelection(payeurPosition);
            List<Participant> participantList = Participant.findByExpenditureId(DBHELPER, depenseId);
            for(Participant p: participantList){
                long memberId = p.getMemberId();
                int memberPosition = getPosition(membersIDs, memberId);
                participantsListView.setItemChecked(memberPosition, true);
            }
            titleET.setInputType(InputType.TYPE_NULL);
            sommeET.setInputType(InputType.TYPE_NULL);
            dateET.setInputType(InputType.TYPE_NULL);
            payeurSpinner.setEnabled(false);
            getView().findViewById(R.id.save_depense_btn).setVisibility(View.INVISIBLE);
            getView().findViewById(R.id.save_depense_btn).setEnabled(false);
        }
    }

    private int getPosition(long[] list, long v){
        int i= 0;
        while (i<list.length){
            if(list[i] == v){
                break;
            }
            else {
                i++;
            }
        }
        if(i == list.length) i=-1;
        return i;
    }
}