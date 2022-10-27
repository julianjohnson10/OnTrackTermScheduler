package android.julian.ontracktermscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.julian.ontracktermscheduler.Entity.Course;
import android.julian.ontracktermscheduler.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    static Context context;
    public static ArrayList<Course> mCourses;

    public CourseAdapter(Context ctx, ArrayList<Course> mCourses){
        this.context = ctx;
        this.mCourses = mCourses;
    }

public static  class CourseViewHolder extends RecyclerView.ViewHolder{
    TextView courseName;
    CardView cardView;

    public CourseViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView2);

        courseName = (TextView) itemView.findViewById(R.id.courseName);

        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int pos=getAdapterPosition();
                final Course current=mCourses.get(pos);
                Intent intent=new Intent(context, CourseDetail.class);
                intent.putExtra("course_id", current.getCourseID());
                intent.putExtra("course_title", current.getCourseTitle());
                intent.putExtra("course_start",current.getStartDate());
                intent.putExtra("course_end",current.getEndDate());
                intent.putExtra("course_status", current.getStatus());
                intent.putExtra("course_notes",current.getCourseNotes());
                intent.putExtra("course_instructor_name", current.getInstructorName());
                intent.putExtra("course_instructor_phone", current.getInstructorPhone());
                intent.putExtra("course_instructor_email", current.getInstructorEmail());

                context.startActivity(intent);
            }
        });
    }
}

    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.course_list_item, parent, false);
        return new CourseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if(mCourses!=null){
            Course current=mCourses.get(position);
            String name=current.getCourseTitle();
            holder.courseName.setText(name);
        }
        else{
            holder.courseName.setText("No courses");
        }
        holder.cardView.setTag(position);
    }

    public void setCourses(ArrayList<Course> courses){
        mCourses=courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mCourses!=null){
            return mCourses.size();
        }
        else{
            return 0;
        }
    }
}
