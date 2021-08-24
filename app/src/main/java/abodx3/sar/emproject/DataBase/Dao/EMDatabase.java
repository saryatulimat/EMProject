package abodx3.sar.emproject.DataBase.Dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

import abodx3.sar.emproject.DataBase.Modle.Recourd;

@Database(entities = {Recourd.class}, exportSchema = false, version = 1)
public abstract class EMDatabase extends RoomDatabase {

    public List<Recourd> r;

    public abstract RecourdsQuery RecourdsQuery();

    public static EMDatabase getConnection(Context c) {
        return Room.databaseBuilder(c,
                EMDatabase.class, "EM.db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }
}