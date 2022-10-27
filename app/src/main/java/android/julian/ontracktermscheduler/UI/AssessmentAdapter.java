package android.julian.ontracktermscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.julian.ontracktermscheduler.Entity.Assessment;
import android.julian.ontracktermscheduler.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    static Context context;
    public static ArrayList<Assessment> mAssessments;

    public AssessmentAdapter(Context ctx, ArrayList<Assessment> mAssessments){
        this.context = ctx;
        this.mAssessments = mAssessments;
    }

    public static  class AssessmentViewHolder extends RecyclerView.ViewHolder{
        TextView assessmentTitle;
        CardView cardView;

        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView3);

            assessmentTitle = (TextView) itemView.findViewById(R.id.assessmentTitle);

            cardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    final Assessment current=mAssessments.get(pos);
                    Intent intent=new Intent(context, AssessmentDetail.class);
                    intent.putExtra("assessment_id", current.getAssessmentID());
                    intent.putExtra("assessment_title", current.getAssessmentTitle());
                    intent.putExtra("assessment_end",current.getAssessmentEndDate());
                    intent.putExtra("assessment_start",current.getAssessmentStartDate());
                    intent.putExtra("assessment_type",current.getAssessmentType());
                    intent.putExtra("course_id",current.getCourseID());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View v = mInflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if(mAssessments!=null){
            Assessment current=mAssessments.get(position);
            String name=current.getAssessmentTitle();
            holder.assessmentTitle.setText(name);
        }
        else{
            holder.assessmentTitle.setText("No assessments");
        }
        holder.cardView.setTag(position);
    }

    public void setAssessments(ArrayList<Assessment> assessments){
        mAssessments=assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mAssessments!=null){
            return mAssessments.size();
        }
        else{
            return 0;
        }
    }
}
