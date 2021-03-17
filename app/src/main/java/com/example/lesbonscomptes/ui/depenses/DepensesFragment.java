package com.example.lesbonscomptes.ui.depenses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.lesbonscomptes.R;

public class DepensesFragment extends Fragment {

    private DepensesViewModel depensesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        depensesViewModel =
                new ViewModelProvider(this).get(DepensesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_depenses, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        depensesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}