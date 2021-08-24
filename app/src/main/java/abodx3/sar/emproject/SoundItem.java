package abodx3.sar.emproject;


import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.frolo.waveformseekbar.WaveformSeekBar;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;

import java.io.File;
import java.io.IOException;

import abodx3.sar.emproject.DataBase.Dao.EMDatabase;
import abodx3.sar.emproject.DataBase.Modle.Recourd;
import abodx3.sar.emproject.HttpAPI.UplaodWav;
import abodx3.sar.emproject.audiomaneger.AmplitudExtractor;
import abodx3.sar.emproject.audiomaneger.AudioPlayer;
import at.markushi.ui.CircleButton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SoundItem extends RecyclerView.ViewHolder implements View.OnClickListener, Callback, WaveformSeekBar.OnSeekBarChangeListener, Player.Listener {


    public final View view;
    WaveformSeekBar waveBar;
    CircleButton playbtn;
    Recourd recourd;
    AudioPlayer player;
    TextView textView;
    Runnable runnable;
    Handler h = new Handler();
    int x;

    public SoundItem(View view) {
        super(view);
        this.view = view;
        waveBar = (WaveformSeekBar) view.findViewById(R.id.progressbar);
        playbtn = (CircleButton) view.findViewById(R.id.playbutton);
        textView = (TextView) view.findViewById(R.id.emtext);
    }

    public void refreshEM() {
        if (recourd != null && (recourd.emotion == null || recourd.emotion.equals(""))) {
            UplaodWav predector = new UplaodWav();
            predector.inituploadFile("http://192.168.1.7", new File(recourd.getFullPath()));
            predector.exec(this);
        }
    }

    public void setRecourd(Recourd recourd) {
        this.recourd = recourd;
        refreshEM();
        if (recourd.emotion != null)
            textView.setText(recourd.emotion);
        waveBar.setWaveform(new AmplitudExtractor().getAmplituds(view.getContext(), recourd.getFullPath()));
        playbtn.setOnClickListener(this);
        waveBar.setOnSeekBarChangeListener(this);

    }

    void initPlayer() {
        if (player == null || player != AudioPlayer.topPlayer) {
            if (AudioPlayer.topPlayer != null) {
                AudioPlayer.topPlayer.pause();
                AudioPlayer.topPlayer.release();
            }
            player = new AudioPlayer(view.getContext(), recourd.getFullPath());
            player.addListener(this);
        }
        AudioPlayer.topPlayer = player;
    }


    //region http
    @Override
    public void onFailure(final Call call, final IOException e) {

    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        if (response.isSuccessful()) {
            recourd.emotion = response.body().string();
            textView.setText(recourd.emotion);
            EMDatabase db = EMDatabase.getConnection(view.getContext());
            db.RecourdsQuery().Update(recourd);
            db.close();
        } else {
            textView.setText("Err");
        }
    }

    //endregion


    public View getView() {
        return view;
    }

    public String getPath() {
        return recourd.getFullPath();
    }

    public void playHandler() {

        runnable = new Runnable() {
            @Override
            public void run() {

                if (player != null) {
                    waveBar.setProgressInPercentage(player.getProgressInPercentage());
                    if (player.isPlaying())
                        h.postDelayed(runnable, 60);
                }
            }
        };
        h.post(runnable);
    }

    public void play() {

        if (!(new File(recourd.getFullPath()).exists()))
            Toast.makeText(view.getContext(), "الملف  غير موجود", Toast.LENGTH_SHORT).show();
        else {
            initPlayer();
            player.prepare();
            player.start();
        }
    }


    //region init


    //endregion

    //region implements WaveformSeekBar.OnSeekBarChangeListener
    @Override
    public void onProgressInPercentageChanged(WaveformSeekBar seekBar, float percent, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(WaveformSeekBar seekBar) {

    }


    @Override
    public void onStopTrackingTouch(WaveformSeekBar seekBar) {
        if (player != null) {
            if (player != AudioPlayer.topPlayer)
                initPlayer();
            player.seekTo(Long.parseLong(Math.round(seekBar.getProgressPercent() * player.getDuration()) + ""));
            play();


        }
    }
    //endregion

    //region implements Player.Listener
    @Override
    public void onIsPlayingChanged(boolean isPlaying) {

        String src = "@drawable/paush";
        if (!isPlaying) {
            src = "@drawable/play";
        } else
            playHandler();
        int imageres = view.getContext().getResources().getIdentifier(src, null, view.getContext().getPackageName());
        playbtn.setImageDrawable(view.getContext().getResources().getDrawable(imageres));
    }

    @Override
    public void onPlaybackStateChanged(int state) {
        if (state == ExoPlayer.STATE_ENDED) {
            player.gotToStart();
        }
    }
    //endregion

    @Override
    public String toString() {
        return "";
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        initPlayer();
        if (player != null) {
            if (!player.isPlaying()) {
                play();
            } else {
                player.pause();
            }
        }
    }
}

