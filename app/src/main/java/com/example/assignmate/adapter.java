package com.example.assignmate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;

import android.net.Uri;
import java.io.File;
import com.pdftron.pdf.config.ViewerConfig;
import com.pdftron.pdf.controls.DocumentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.IdentityHashMap;

public class adapter extends FirebaseRecyclerAdapter<file_model,adapter.ViewHolder> {
    RecyclerView recyclerView;
    public static final String URI = "";

    ProgressBar progressBar;
    Context context;

    ImageView image;
    FirebaseRecyclerOptions<file_model> options;

    TextView empty;
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
                String URL = model.getUrl();
                Toast.makeText(context, "Please wait..", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                    intent.setType(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(model.getUrl()));
//                    intent.setPackage("com.google.android.apps.photos");

                String fileName = model.getFile_Name();
                String ext = fileName.substring(fileName.lastIndexOf("."));
                if (ext.equals(".pdf")||ext.equals(".doc") || ext.equals(".docx")||ext.equals(".jpg") || ext.equals(".jpeg")||ext.equals(".png") ||(ext.equals(".txt"))||(ext.equals(".ppt")||ext.equals(".pptx"))) {

                       ViewerConfig config = new ViewerConfig.Builder().openUrlCachePath(view.getContext().getCacheDir().getAbsolutePath()).build();
                       final Uri fileLink = Uri.parse(model.getUrl());
                        DocumentActivity.openDocument(view.getContext(), fileLink, config);

                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW,url);
                    context.startActivity(intent);
                }

//
            }
        });

    }




    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView descrption,name,timestamp;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.progressBarID);
            name=itemView.findViewById(R.id.put_filename);
            descrption=itemView.findViewById(R.id.put_description);
            timestamp = itemView.findViewById(R.id.timestamp);

            empty = itemView.findViewById(R.id.empty);
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
