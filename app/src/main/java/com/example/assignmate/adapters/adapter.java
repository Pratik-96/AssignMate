package com.example.assignmate.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmate.Models.file_model;
import com.example.assignmate.R;
import com.example.assignmate.pdfActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


//import com.pdftron.pdf.config.ViewerConfig;
//import com.pdftron.pdf.controls.DocumentActivity;
//import com.rajat.pdfviewer.PdfViewerActivity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class adapter extends FirebaseRecyclerAdapter<file_model,adapter.ViewHolder> {
    RecyclerView recyclerView; 
    public static final String URI = "";

    ProgressBar progressBar;
    Context context;

    ImageView image;
    FirebaseRecyclerOptions<file_model> options;

    TextView empty;
    public static final String URL = "";
    ArrayList<String> urls = new ArrayList<>();

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public adapter(@NonNull FirebaseRecyclerOptions<file_model> options,Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create views for recycler view items
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_design,parent,false);
        return new adapter.ViewHolder(view); //return object of viewholder  which contains all the views

    }
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        //Initialize the elements of individual objects
//        file_model model = items.get(position)
//        holder.name.setText(model.getFile_Name());
//        holder.descrption.setText(model.getDescription());
//
//    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull file_model model) {
        holder.name.setText(model.getFile_Name());
        holder.descrption.setText("Description:- "+model.getDescription());
        holder.timestamp.setText(model.getTimeStamp());



        urls.add(model.getUrl());
        String fileName = model.getFile_Name();
        String ext = fileName.substring(fileName.lastIndexOf("."));

        if (ext.equals(".doc") || ext.equals(".docx")) {
            holder.imageView.setImageResource(R.drawable.document_icon);
        }else
            if (ext.equals(".png"))
            {
                holder.imageView.setImageResource(R.drawable.png_icon);

            }
            else
        if (ext.equals(".jpg") || ext.equals(".jpeg")) {
            holder.imageView.setImageResource(R.drawable.jpg_icon);
        }else
        if (ext.equals(".ppt")||ext.equals(".pptx"))
        {
            holder.imageView.setImageResource(R.drawable.ppt_icon);

        }else
        if (ext.equals(".txt"))
        {
            holder.imageView.setImageResource(R.drawable.txt_icon);
        }
        else
        if (ext.equals(".pdf")) {
            holder.imageView.setImageResource(R.drawable.pdf_icon);
        }
        else
        {
            holder.imageView.setImageResource(R.drawable.unknown_doc);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Uri url = Uri.parse(model.getUrl());
                Toast.makeText(context, "Please wait..", Toast.LENGTH_SHORT).show();
                String fileName = model.getFile_Name();
                String ext = fileName.substring(fileName.lastIndexOf("."));
                if (ext.equals(".pdf")) {
                    Intent intent = new Intent(context, pdfActivity.class);
                    intent.putExtra("URL",model.getUrl());
                    intent.putExtra("NAME",model.getFile_Name());
                    intent.putExtra("DESCRIPTION",model.getDescription());
                    view.getContext().startActivity(intent);

                    }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW,url);
                    view.getContext().startActivity(intent);
                }

//
            }
        });

    }

private int lastPosition=-1;
    public void setAnimation(View view,int position)
    {

            Animation slideIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.setAnimation(slideIn);

    }





    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView descrption,name,timestamp;
        ImageView imageView;

        LinearLayout item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.progressBarID);
            name=itemView.findViewById(R.id.put_filename);
            descrption=itemView.findViewById(R.id.put_description);
            timestamp = itemView.findViewById(R.id.timestamp);
            item=itemView.findViewById(R.id.item);


            imageView= itemView.findViewById(R.id.doc_icon);


        }

    }



    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (progressBar!=null)
        {
            progressBar.setVisibility(View.GONE);

        }
    }
}
