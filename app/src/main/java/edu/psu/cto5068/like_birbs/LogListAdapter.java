package edu.psu.cto5068.like_birbs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LogListAdapter extends ArrayAdapter<LogMsg> {
    private static final String TAG = "LogListAdapter";
    private Context mContext;
    int mResource;

    public LogListAdapter(@NonNull Context context, int resource, @NonNull List<LogMsg> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String msg = getItem(position).getMsg();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView msgView = convertView.findViewById(R.id.msgView);

        msgView.setText(msg);

        return convertView;
    }
}
