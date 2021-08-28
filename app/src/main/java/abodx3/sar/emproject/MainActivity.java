package abodx3.sar.emproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import java.io.File;
import java.io.IOException;

import abodx3.sar.emproject.DataBase.Dao.EMDatabase;
import abodx3.sar.emproject.DataBase.Modle.Recourd;
import abodx3.sar.emproject.audiomaneger.AudioRecourder;
import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AudioRecourder.OnAnimateVoice, AudioRecourder.OnRecourding {
    //region permission
    final int RECORD_AUDIO = 1;
    final int READ_EXTERNAL_STORAGE = 2;
    final int WRITE_EXTERNAL_STORAGE = 3;


    void requestAllPermission() {
        requestPermission(Manifest.permission.RECORD_AUDIO, RECORD_AUDIO);
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
    }

    public void requestPermission(String permission, int PERMISSION_ID) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_ID);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RECORD_AUDIO || requestCode == READ_EXTERNAL_STORAGE || requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }
    //endregion

    TextView timeLable;
    SpinKitView waveSpin;
    CircleButton RecordButton;
    AudioRecourder recourder;
    RecourdFragment fragment;
    ImageButton attachbtn;
    Switch them;
    boolean x = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestAllPermission();
        initViews();
    }

    public void animateVoice(final float maxPeak) {
        if (RecordButton != null) {
            RecordButton.animate().scaleX(1 + maxPeak).scaleY(1 + maxPeak).setDuration(10).start();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 15:
                if (resultCode == RESULT_OK) {
                    File FilePath = new File(RealPathUtil.getRealPath(getApplicationContext(), data.getData()));
                    String name = FilePath.getAbsoluteFile().getName();
                    String folder = FilePath.getAbsoluteFile().getParent();
                    Recourd newRecourd = new Recourd(name, folder);
                    commiteRecourd(newRecourd);
                }
                break;

        }
    }

    void initViews() {

        them = (Switch) findViewById(R.id.theem);
        them.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

            }
        });
        timeLable = (TextView) findViewById(R.id.timelable);
        RecordButton = (CircleButton) findViewById(R.id.recordbutton);
        waveSpin = (SpinKitView) findViewById(R.id.wavSpin);
        recourder = new AudioRecourder(RecordButton);
        attachbtn = (ImageButton) findViewById(R.id.attbtn);
        attachbtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("file/*");
            startActivityForResult(intent, 15);
        });
        fragment = new RecourdFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.soundList, fragment).commit();
        RecordButton.setOnClickListener(this);


        recourder.setOnAnimateVoice(this);
        recourder.setOnRecourding(this);

        waveSpin.setEnabled(false);
        waveSpin.setVisibility(View.INVISIBLE);

        timeLable.setVisibility(View.INVISIBLE);
    }


    public void onClick(View v) {

        if (!recourder.isreco())
            startRecourd();
        else
            StopRecourd("0:00");
    }

    void startRecourd() {
        recourder.start();
        attachbtn.setVisibility(View.INVISIBLE);
        timeLable.setVisibility(View.VISIBLE);
        waveSpin.setEnabled(true);
        waveSpin.setVisibility(View.VISIBLE);
    }

    void StopRecourd(String time) {
        try {
            recourder.stop();
            if (time.startsWith("2")) {
                String folde = recourder.FOLDER_PATH;
                Recourd newRecourd = new Recourd(recourder.getLastFilename(), folde);
                commiteRecourd(newRecourd);
            } else {
                new File(recourder.getLastfileFullpath()).delete();
            }
            attachbtn.setVisibility(View.VISIBLE);
            waveSpin.setEnabled(false);
            waveSpin.setVisibility(View.INVISIBLE);
            timeLable.setVisibility(View.INVISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void commiteRecourd(Recourd newRecourd) {

        EMDatabase db = EMDatabase.getConnection(this);
        if (!db.RecourdsQuery().isEXISTS(newRecourd.name)) {
            db.RecourdsQuery().Insert(newRecourd);
            fragment.addRecourd(newRecourd);
        } else {
            Toast.makeText(this, "يوجد ملف مشابه", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    @Override
    public void whileRecourding(String time) {

        timeLable.setText(time);
        if (time.startsWith("2"))
            StopRecourd(time);
    }
}