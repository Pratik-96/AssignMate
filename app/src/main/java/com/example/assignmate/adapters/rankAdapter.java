package com.example.assignmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmate.Models.Streak_model;
import com.example.assignmate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.Collections;

public class rankAdapter extends FirebaseRecyclerAdapter<Streak_model, rankAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param context
     */

    Context context;

    ArrayList<Streak_model> list;
    public rankAdapter(@NonNull FirebaseRecyclerOptions<Streak_model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Streak_model model) {




        holder.userRank.setText(String.valueOf(position+1));
        holder.streak.setText(String.valueOf(model.getLongestStreak()));

        if (model.getUname()==null)
        {
            holder.UserName.setText("User");
        }
        else {
            holder.UserName.setText(model.getUname());
        }


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_layout,parent,false);
        return new rankAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userRank,UserName,streak;
        ConstraintLayout cl;
        int rank =1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             userRank = itemView.findViewById(R.id.userRank);
             UserName = itemView.findViewById(R.id.leadUserName);
             streak = itemView.findViewById(R.id.userLearningStreak);
             cl = itemView.findViewById(R.id.constraintly);
        }
    }
}
