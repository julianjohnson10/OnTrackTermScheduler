package android.julian.ontracktermscheduler.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import android.julian.ontracktermscheduler.Entity.User;


@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT userName FROM users WHERE userName = :userName")
    String checkUserName(String userName);

    @Query("SELECT * FROM users WHERE userName = :userName")
    User getUser(String userName);

}
