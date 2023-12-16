package com.example.assignmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmate.Models.sem_model;
import com.example.assignmate.R;
import com.example.assignmate.documentType;
import com.example.assignmate.fetch_files;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class sem_adapter extends FirebaseRecyclerAdapter<sem_model, sem_adapter.ViewHolder> {


    Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public sem_adapter(@NonNull FirebaseRecyclerOptions<sem_model> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull sem_adapter.ViewHolder holder, int position, @NonNull sem_model model) {
        Picasso.get().load(model.getImg_url()).into(holder.imageView);
        holder.textView.setText(model.getSub_name());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sub_name = model.getSub_name();


                switch (sub_name) {
                    case "Activities":
                        Intent intent = new Intent(context, fetch_files.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("docType", " ");
                        bundle.putString("name", "Activities");
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        break;
                    case "Placement Preparation":
                        Intent intent1 = new Intent(context, fetch_files.class);
                        bundle = new Bundle();
                        bundle.putString("docType", "Placement");
                        bundle.putString("name", "Placement Preparation");
                        intent1.putExtras(bundle);
                        context.startActivity(intent1);
                        break;
                    default: Intent intent2 = new Intent(context, documentType.class);
                        intent2.putExtra("SUBJECT_NAME", model.getSub_name());
                        context.startActivity(intent2);
                        break;
                }

            }
        });

    }

    @NonNull
    @Override
    public sem_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_layout, parent, false);

        return new sem_adapter.ViewHolder(view);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        LinearLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.sub_img);
            textView = itemView.findViewById(R.id.sub_name);
            card = itemView.findViewById(R.id.Card);

        }
    }
}
