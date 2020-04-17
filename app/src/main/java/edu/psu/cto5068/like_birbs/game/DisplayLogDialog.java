package edu.psu.cto5068.like_birbs.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DisplayLogDialog extends DialogFragment {

    public interface LogDialogListener {
        void onLogDialogPositiveClick(DialogFragment dialog);
        void onLogDialogNegativeClick(DialogFragment dialog);
    }

    LogDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (LogDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(this.toString() + " must implement LogDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // TODO: get log
        String message = getArguments().getString("message");

        builder
                .setTitle("Birb Logs")
//                .setView() // TODO: fill with log list
                .setPositiveButton("View Death Logs", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onLogDialogPositiveClick(DisplayLogDialog.this);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onLogDialogNegativeClick(DisplayLogDialog.this);
                    }
                });
        return builder.create();
    }
}
