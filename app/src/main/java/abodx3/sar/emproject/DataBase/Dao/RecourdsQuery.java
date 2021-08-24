package abodx3.sar.emproject.DataBase.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import abodx3.sar.emproject.DataBase.Modle.Recourd;

@Dao
public interface RecourdsQuery {

    @Query("select EXISTS(SELECT * FROM recourd WHERE name = :name)")
    public boolean isEXISTS(String name);

    @Query("Select * from recourd")
    public List<Recourd> getAllRecourds();

    @Query("SELECT * FROM recourd WHERE name like :Id LIMIT 1")
    public Recourd loadById(String Id);

    @Query("SELECT * FROM recourd WHERE emotion = :EM")
    public List<Recourd> loadByEmotion(String EM);

    @Insert
    public Void Insert(Recourd recourd);

    @Insert
    public Void Insertall(Recourd... recourds);

    @Update
    public Void Update(Recourd recourd);

    @Update
    public Void UpdateAll(Recourd... recourds);

    @Delete
    public Void deleteAll(Recourd... recourds);

    @Delete
    public Void delete(Recourd recourd);

}
