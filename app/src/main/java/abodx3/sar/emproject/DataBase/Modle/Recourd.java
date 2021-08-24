package abodx3.sar.emproject.DataBase.Modle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.android.exoplayer2.upstream.LoaderErrorThrower;

import java.io.File;

@Entity(tableName = "recourd")

public class Recourd {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "folderpath")
    public String folderpath;
    @ColumnInfo(name = "emotion")
    public String emotion;

    public Recourd(String name, String folderpath, String emotion) {

        this.name = name;
        this.folderpath = folderpath;
        this.emotion = emotion;
    }

    @Ignore
    public Recourd(String name, String folderPath) {
        this.name = name;
        this.folderpath = folderPath;
    }

    @Ignore
    public String getFullPath() {
        return new File(folderpath, name).getPath();
    }

    @Override
    public String toString() {
        return getFullPath();
    }
}
