package android.julian.ontracktermscheduler.Database;

import android.app.Application;
import android.julian.ontracktermscheduler.DAO.AssessmentDAO;
import android.julian.ontracktermscheduler.DAO.CourseDAO;
import android.julian.ontracktermscheduler.DAO.TermDAO;
import android.julian.ontracktermscheduler.DAO.UserDAO;
import android.julian.ontracktermscheduler.Entity.Assessment;
import android.julian.ontracktermscheduler.Entity.Course;
import android.julian.ontracktermscheduler.Entity.Term;
import android.julian.ontracktermscheduler.Entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private final TermDAO mTermDAO;
    private final CourseDAO mCourseDAO;
    private final AssessmentDAO mAssessmentDAO;
    private final UserDAO mUserDAO;
    private List<Term> mAllTerms;
    private List<Course> mAllCourses;
    private List<Assessment> mAllAssessments;
    private User mUser;
    private  String mUserName;

    private static final int THREADS=10;
    static final ExecutorService dbExecutor= Executors.newFixedThreadPool(THREADS);

    public Repository(Application application){
        DatabaseBuilder db=DatabaseBuilder.getDatabase(application);
        mCourseDAO=db.courseDAO();
        mTermDAO=db.termDAO();
        mAssessmentDAO=db.assessmentDAO();
        mUserDAO=db.userDAO();
    }

    public ArrayList<Term>getAllTerms(){
        dbExecutor.execute(()->{
            mAllTerms=mTermDAO.getAllTerms();
        });

        try{
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ArrayList<Term>) mAllTerms;
    }

    public String checkUserName(String userName){
        dbExecutor.execute(()->{
            mUserName=mUserDAO.checkUserName(userName);
        });

        try{
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return mUserName;
    }

    public User getUser(String userName){
        dbExecutor.execute(()->{
            mUser=mUserDAO.getUser(userName);
        });

        try{
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (User) mUser;
    }

    public ArrayList<Course>getCoursesFromTerm(int termID){
        dbExecutor.execute(()->{
            mAllCourses=mCourseDAO.getCoursesFromTerm(termID);
        });

        try{
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ArrayList<Course>) mAllCourses;
    }

    public ArrayList<Course>searchCourse(int termID, String courseTitle){
        dbExecutor.execute(()->{
            mAllCourses=mCourseDAO.searchCourse(termID, courseTitle);
        });

        try{
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ArrayList<Course>) mAllCourses;
    }

    public ArrayList<Course>getAllCourses(){
        dbExecutor.execute(()->{
            mAllCourses=mCourseDAO.getAllCourses();
        });

        try{
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ArrayList<Course>) mAllCourses;
    }

    public ArrayList<Assessment>getAllAssessments(){
        dbExecutor.execute(()->{
            mAllAssessments = mAssessmentDAO.getAllAssessments();
        });

        try{
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ArrayList<Assessment>) mAllAssessments;
    }
    public ArrayList<Assessment>getAssessmentsFromCourse(int courseID){
        dbExecutor.execute(()->{
            mAllAssessments = mAssessmentDAO.getAssessmentsFromCourse(courseID);
        });

        try{
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (ArrayList<Assessment>) mAllAssessments;
    }

    public void insertTerm(Term term) {
        dbExecutor.execute(() -> {
            mTermDAO.insert(term);
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertUser(User user) {
        dbExecutor.execute(() -> {
            mUserDAO.insert(user);
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertCourse(Course course) {
        dbExecutor.execute(() -> {
            mCourseDAO.insert(course);
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void insertAssessment(Assessment assessment) {
        dbExecutor.execute(() -> {
            mAssessmentDAO.insert(assessment);
        });
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateTerm(Term term){
        dbExecutor.execute(()->{
            mTermDAO.update(term);
        });
        try{
            Thread.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void updateCourse(Course course){
        dbExecutor.execute(()->{
            mCourseDAO.update(course);
        });
        try{
            Thread.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void updateAssessment(Assessment assessment){
        dbExecutor.execute(()->{
            mAssessmentDAO.update(assessment);
        });
        try{
            Thread.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void updateUser(User user){
        dbExecutor.execute(()->{
            mUserDAO.update(user);
        });
        try{
            Thread.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void deleteTerm(Term term){
        dbExecutor.execute(()->{
            mTermDAO.delete(term);
        });
        try{
            Thread.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void deleteCourse(Course course){
        dbExecutor.execute(()->{
            mCourseDAO.delete(course);
        });
        try{
            Thread.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void deleteAssessment(Assessment assessment){
        dbExecutor.execute(()->{
            mAssessmentDAO.delete(assessment);
        });
        try{
            Thread.sleep(10);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
