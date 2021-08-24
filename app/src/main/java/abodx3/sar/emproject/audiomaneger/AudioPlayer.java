package abodx3.sar.emproject.audiomaneger;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.io.File;

public class AudioPlayer extends SimpleExoPlayer {


    public static AudioPlayer topPlayer;

    public AudioPlayer(Context context, String path) {
        super(new SimpleExoPlayer.Builder(context));

        Uri firstVideoUri = Uri.fromFile(new File(path));

        MediaItem firstItem = MediaItem.fromUri(firstVideoUri);

        this.addMediaItem(firstItem);
        this.prepare();
    }


    public void gotToStart() {
        this.seekToDefaultPosition();
        this.pause();
    }

    public void start() {
        if (topPlayer != null) {
            topPlayer.pause();
        }
        topPlayer = this;
        if (!this.isPlaying()) {
            this.play();
        }

    }

    public float getProgressInPercentage() {
        return this.getCurrentPosition() / Float.parseFloat(this.getDuration() + "");
    }


}
