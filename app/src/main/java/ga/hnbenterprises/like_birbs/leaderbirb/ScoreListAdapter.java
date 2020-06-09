package ga.hnbenterprises.like_birbs.leaderbirb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ga.hnbenterprises.like_birbs.R;

public class ScoreListAdapter extends ArrayAdapter<HighScore> {
    private static final String TAG = "ScoreListAdapter";
    private Context mContext;
    int mResource;

    public ScoreListAdapter(@NonNull Context context, int resource, @NonNull List<HighScore> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int rank = getItem(position).getRank();
        String username = getItem(position).getUsername();
        String env = getItem(position).getEnv();
        int score = getItem(position).getScore();
        boolean isHighlighted = getItem(position).isHighlight();
        HighScore highScore = new HighScore(rank, username, env, score, isHighlighted);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        LinearLayout highscoreView = convertView.findViewById(R.id.highscoreView);

        if (isHighlighted) {
            highscoreView.setBackgroundColor(-16711936);
        }

        TextView rankView = convertView.findViewById(R.id.rankView);
        TextView nameView = convertView.findViewById(R.id.nameView);
        TextView envView = convertView.findViewById(R.id.envView);
        TextView scoreView = convertView.findViewById(R.id.scoreView);

        rankView.setText(Integer.toString(rank));
        nameView.setText(username);
        envView.setText(env);
        scoreView.setText(Integer.toString(score));

        return convertView;
    }
}
