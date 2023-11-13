package com.example.assignmate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmate.Models.sem_model;
import com.example.assignmate.adapters.adapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class sem_adapter extends FirebaseRecyclerAdapter<sem_model,sem_adapter.ViewHolder> {


    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public sem_adapter(@NonNull FirebaseRecyclerOptions<sem_model> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull sem_adapter.ViewHolder holder, int position, @NonNull sem_model model) {
        Picasso.get().load(model.getImg_url()).into(holder.imageView);
        holder.textView.setText(model.getSub_name());
    }

    @NonNull
    @Override
    public sem_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_layout,parent,false);

        return new sem_adapter.ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             imageView = itemView.findViewById(R.id.sub_img);
             textView = itemView.findViewById(R.id.sub_name);

        }
    }
}
