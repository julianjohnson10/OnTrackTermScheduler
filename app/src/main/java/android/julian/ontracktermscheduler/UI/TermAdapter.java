package android.julian.ontracktermscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.julian.ontracktermscheduler.Entity.Term;
import android.julian.ontracktermscheduler.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    public TermAdapter(Context context, ArrayList<Term> mTerms){
        this.context = context;
        this.mTerms = mTerms;
    }

public static class TermViewHolder extends RecyclerView.ViewHolder{

    TextView termName;
    CardView cardView;



    public TermViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cardView);

        termName = itemView.findViewById(R.id.termName);

        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int pos=getAdapterPosition();
                final Term current=mTerms.get(pos);
                Intent intent=new Intent(context, TermDetails.class);
                intent.putExtra("term_id", current.getTermID());
                intent.putExtra("term_name", current.getTermName());
                intent.putExtra("term_start",current.getStartDate());
                intent.putExtra("term_end",current.getEndDate());

                context.startActivity(intent);
            }
        });

    }

}

    public static ArrayList<Term> mTerms;
    static Context context;

    @NonNull
    @Override
    public TermAdapter.TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        if(mTerms!=null){
            Term current=mTerms.get(position);
            String name=current.getTermName();
            holder.termName.setText(name);
        }
        else{
            holder.termName.setText("No terms");
        }
        holder.cardView.setTag(position);
    }

    public void setTerms(ArrayList<Term> terms){
        mTerms=terms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mTerms!=null){
            return mTerms.size();
        }
        else{
            return 0;
        }
    }
}
