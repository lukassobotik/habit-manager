package com.sforge.habitsprototype4;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SecondsToLoadDialog extends AppCompatDialogFragment {

    private Button oneSec, twoSec, threeSec, fourSec;
    private SecondsToLoadDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_settings_seconds_to_load, null);

        builder.setView(view).setTitle("Seconds To Load");

        oneSec = view.findViewById(R.id.dialog_one_second);
        twoSec = view.findViewById(R.id.dialog_two_seconds);
        threeSec = view.findViewById(R.id.dialog_three_seconds);
        fourSec = view.findViewById(R.id.dialog_four_seconds);

        oneSec.setOnClickListener(view1 -> {
            int option = 500;
            getDialog().cancel();
            listener.getData(option);
        });
        twoSec.setOnClickListener(view1 -> {
            int option = 750;
            getDialog().cancel();
            listener.getData(option);
        });
        threeSec.setOnClickListener(view1 -> {
            int option = 1000;
            getDialog().cancel();
            listener.getData(option);
        });
        fourSec.setOnClickListener(view1 -> {
            int option = 2000;
            getDialog().cancel();
            listener.getData(option);
        });

        return builder.create();
    }

    public int returnSelectedOption(){
        final int[] option = new int[1];
        option[0] = 0;
        oneSec.setOnClickListener(view1 -> {
            option[0] = 1;
            getDialog().cancel();
        });
        twoSec.setOnClickListener(view1 -> {
            option[0] = 2;
            getDialog().cancel();
        });
        threeSec.setOnClickListener(view1 -> {
            option[0] = 3;
            getDialog().cancel();
        });
        fourSec.setOnClickListener(view1 -> {
            option[0] = 4;
            getDialog().cancel();
        });

        return option[0];
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (SecondsToLoadDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " Must implement SecondsToLoadDialogListener");
        }
    }

    public interface SecondsToLoadDialogListener{
        void getData(int option);
    }
}
