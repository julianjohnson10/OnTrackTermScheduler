package android.julian.ontracktermscheduler.Database;

import android.content.Context;
import android.julian.ontracktermscheduler.DAO.AssessmentDAO;
import android.julian.ontracktermscheduler.DAO.CourseDAO;
import android.julian.ontracktermscheduler.DAO.TermDAO;
import android.julian.ontracktermscheduler.DAO.UserDAO;
import android.julian.ontracktermscheduler.Entity.Assessment;
import android.julian.ontracktermscheduler.Entity.Course;
import android.julian.ontracktermscheduler.Entity.Term;
import android.julian.ontracktermscheduler.Entity.User;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Term.class, Course.class, Assessment.class, User.class}, version=10, exportSchema = false)
public abstract class DatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();

    public abstract CourseDAO courseDAO();

    public abstract AssessmentDAO assessmentDAO();

    public abstract UserDAO userDAO();

    private static volatile DatabaseBuilder INSTANCE;

    static DatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), DatabaseBuilder.class, "schedulerDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}