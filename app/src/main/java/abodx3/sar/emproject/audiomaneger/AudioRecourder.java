package abodx3.sar.emproject.audiomaneger;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import at.markushi.ui.CircleButton;
import omrecorder.AudioChunk;
import omrecorder.AudioRecordConfig;
import omrecorder.OmRecorder;
import omrecorder.PullTransport;
import omrecorder.PullableSource;
import omrecorder.Recorder;

public class AudioRecourder {

    final int TIMER_DELAY = 100;
    final public String FOLDER_PATH = Environment.getExternalStorageDirectory() + File.separator + "EMSound";

    //region interfaces
    public interface OnAnimateVoice {
        public void animateVoice(final float maxPeak);
    }

    public interface OnRecourding {
        public void whileRecourding(String time);
    }
    //endregion

    private OnAnimateVoice animateVoice;
    private OnRecourding recourding;

    private String lastfile = "";
    Recorder audioRecord;
    CircleButton RecordButton;
    Runnable runnable;
    boolean reco = false;

    public void setOnAnimateVoice(OnAnimateVoice animateVoice) {
        this.animateVoice = animateVoice;
    }

    public void setOnRecourding(OnRecourding recourding) {
        this.recourding = recourding;
    }

    public String getLastfileFullpath() {
        return new File(FOLDER_PATH, lastfile).getPath();
    }


    public String getLastFilename() {
        return lastfile;
    }

    public AudioRecourder(CircleButton RecordButton) {
        createFolder();
        this.RecordButton = RecordButton;
    }

    public void init() {

        audioRecord = OmRecorder.wav(
                new PullTransport.Default(mic(), new PullTransport.OnAudioChunkPulledListener() {
                    @Override
                    public void onAudioChunkPulled(AudioChunk audioChunk) {
                        if (animateVoice != null)
                            animateVoice.animateVoice((float) (audioChunk.maxAmplitude() / 200.0));
                    }
                }), file());
    }

    public Boolean createFolder() {
        File Dir = new File(FOLDER_PATH);
        Boolean res = true;
        if (!Dir.exists()) {
            res = Dir.mkdirs();
        }
        return res;
    }

    private PullableSource mic() {

        return new PullableSource.Default(
                new AudioRecordConfig.Default(
                        MediaRecorder.AudioSource.MIC, AudioFormat.ENCODING_PCM_16BIT,
                        AudioFormat.CHANNEL_IN_MONO, 22050
                )
        );
    }

    private File file() {
        lastfile = System.currentTimeMillis() + ".wav";
        File s = new File(FOLDER_PATH, lastfile);
        return s;
    }


    public void start() {
        createFolder();
        init();
        Handler handler = new Handler();
        runnable = new Runnable() {
            long x = 0;

            String getTiemString() {
                x += TIMER_DELAY;
                long time = x;
                long ms = time % 1000;
                ms /= 10;
                time /= 1000;
                long s = time % 60;
                if (reco)
                    return s + ":" + ms;
                else return "0:0";
            }

            @Override
            public void run() {
                if (reco && recourding != null)
                    recourding.whileRecourding(getTiemString());
                if (reco)
                    handler.postDelayed(runnable, TIMER_DELAY);

            }
        };
        reco = true;
        handler.postDelayed(runnable, TIMER_DELAY);
        audioRecord.startRecording();
    }

    public boolean isreco() {
        return reco;
    }

    public void stop() throws IOException {
        reco = false;
        audioRecord.stopRecording();
        if (animateVoice != null)
            animateVoice.animateVoice(0);
    }
}
