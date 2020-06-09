package ga.hnbenterprises.like_birbs;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class SoundService extends Service {
    MediaPlayer player;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        int randomSong = (int) (Math.random() * 3);
        switch(randomSong) {
            case 0:
                player = MediaPlayer.create(this, R.raw.serenity);
                break;
            case 1:
                player = MediaPlayer.create(this, R.raw.home);
                break;
            case 2:
                player = MediaPlayer.create(this, R.raw.freshair);
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int randomSong = (int) (Math.random() * 3);
                switch(randomSong) {
                    case 0:
                        player = MediaPlayer.create(SoundService.this, R.raw.serenity);
                        break;
                    case 1:
                        player = MediaPlayer.create(SoundService.this, R.raw.home);
                        break;
                    case 2:
                        player = MediaPlayer.create(SoundService.this, R.raw.freshair);
                }
            player.start();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        player = null;
        super.onDestroy();
    }
}
