//package com.example.assignmate;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//import java.util.ConcurrentModificationException;
//
//public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {
//    RecyclerView recyclerView;
//
//    Context context;
//
//    ArrayList<String> items = new ArrayList<>();
//
//
//    ArrayList<String> urls = new ArrayList<>();
//
//
//
//
//    public adapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
//        this.recyclerView = recyclerView;
//        this.context = context;
//        this.items = items;
//        this.urls = urls;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        //create views for recycler view items
//        View view = LayoutInflater.from(context).inflate(R.layout.document_design,parent,false);
//        return new ViewHolder(view); //return object of viewholder  which contains all the views
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.name.setText(model.getFile_Name());
//        holder.descrption.setText(model.getDescription());
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position,file_model model) {
//        //Initialize the elements of individual objects
//        holder.name.setText(model.getFile_Name());
//        holder.descrption.setText(model.getDescription());
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//
//        //Return number of items
//        return items.size();
//    }
//    public void update(String name,String description,String url)
//    {
//        items.add(name);
//        items.add(description);
//        urls.add(url);
//        notifyDataSetChanged();     //Refresh
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView descrption,name;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            name=itemView.findViewById(R.id.put_filename);
//            descrption=itemView.findViewById(R.id.put_description);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = recyclerView.getChildAdapterPosition(view);
//                    Intent intent = new Intent();
//                    intent.setType(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(urls.get(position)));
//                    context.startActivity(intent);// This will launch a browser
//                }
//            });
//        }
//    }
//}
