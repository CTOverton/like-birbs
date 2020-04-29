package edu.psu.cto5068.like_birbs.game;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import edu.psu.cto5068.like_birbs.LogListAdapter;
import edu.psu.cto5068.like_birbs.LogMsg;
import edu.psu.cto5068.like_birbs.R;

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

        boolean type = getArguments().getBoolean("deathLogs");
        ArrayList logs = getArguments().getStringArrayList("logs");

        List<LogMsg> logMsgs = new ArrayList<>();
        for (int i = 0; i < logs.size(); i++) {
            logMsgs.add(new LogMsg(logs.get(i).toString()));
        }

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        LogListAdapter adapter = new LogListAdapter(getActivity(), R.layout.logs_adapter_view, logMsgs);

        builder
                .setTitle(type ? "Birb Death Logs" : "Birb Birth Logs")
                .setView(inflater.inflate(R.layout.logs_view, null))
                .setAdapter(adapter, null);
/*                .setPositiveButton(type ? "View Births" : "View Deaths", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onLogDialogPositiveClick(DisplayLogDialog.this);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onLogDialogNegativeClick(DisplayLogDialog.this);
                    }
                });*/
        return builder.create();
    }
}
