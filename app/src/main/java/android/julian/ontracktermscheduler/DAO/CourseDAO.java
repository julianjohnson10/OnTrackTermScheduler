package android.julian.ontracktermscheduler.DAO;

import android.julian.ontracktermscheduler.Entity.Course;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Course course);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);


    @Query("SELECT * FROM courses ORDER BY courseID ASC")
    List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE termID = :termID")
    List<Course> getCoursesFromTerm(int termID);

    @Query("SELECT * FROM courses WHERE termID = :termID AND courseTitle LIKE :courseTitle")
    List<Course> searchCourse(int termID, String courseTitle);

}
