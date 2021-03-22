package com.example.lesbonscomptes.ui.depenses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

import java.util.ArrayList;
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

    // TODO: Rename and change types of parameters
    private ArrayList<String> membersNames;
    private long[] membersIDs;

    public EditDepenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditDepenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditDepenseFragment newInstance(ArrayList<String> param1, long[] param2) {
        EditDepenseFragment fragment = new EditDepenseFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);

        args.putLongArray(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            membersNames = getArguments().getStringArrayList(ARG_PARAM1);
            membersIDs = getArguments().getLongArray(ARG_PARAM2);
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
            SparseBooleanArray participantsChecked = participantsListView.getCheckedItemPositions();
            ArrayList<Long> participantsIds = new ArrayList();
            int i=0;
            while (i<this.membersIDs.length){
                if(participantsChecked.get(i)) participantsIds.add(this.membersIDs[i]);
                i++;
            }
            long payeurId = this.membersIDs[payeurSpinner.getSelectedItemPosition()];
            EditText titleET = getView().findViewById(R.id.sommeET);
            EditText sommeET = getView().findViewById(R.id.sommeET);
            EditText dateET = getView().findViewById(R.id.sommeET);

            float somme = Float.parseFloat(sommeET.getText().toString());
            String title = titleET.getText().toString();
//            Date date = new Date(dateET.getYear(), dateET.getMonth(), dateET.getDayOfMonth());

            Log.d("CREATION-------------", title);
            Log.d("CREATION-------------", ""+somme);
            Log.d("CREATION-------------", dateET.getText().toString());
            Log.d("CREATION-------------", ""+payeurId);
            Log.d("CREATION-------------", participantsIds.toString());

        });
    }
}