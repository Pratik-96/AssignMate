package com.example.assignmate.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmate.MainActivity;
import com.example.assignmate.Models.Todo_model;
import com.example.assignmate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class todoAdapter extends FirebaseRecyclerAdapter<Todo_model, todoAdapter.ViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context context;
    boolean isUpdating=false;

    boolean checkBoxCondition=false;
    public todoAdapter(@NonNull FirebaseRecyclerOptions<Todo_model> options,Context context) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull todoAdapter.ViewHolder holder, int position, @NonNull Todo_model model) {


        holder.checkBox.setChecked(model.isChecked());
        if (holder.checkBox.isChecked())
        {
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }
        else
        {
            holder.textView.setPaintFlags(holder.textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);

        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (holder.checkBox.isChecked()) {
                    checkBoxCondition = true;

                    holder.textView.setPaintFlags(holder.textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    HashMap map = new HashMap();
                    map.put("checked", true);
                    map.put("email", model.getEmail());
                    map.put("text", model.getText());
                    updateData(map, model);
                    Log.d("chk", "onClick: ");
                }
                        else
                        {
                            checkBoxCondition=false;
                            holder.textView.setPaintFlags(holder.textView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                            HashMap map = new HashMap();
                            map.put("checked",false);
                            map.put("email",model.getEmail());
                            map.put("text",model.getText());

                           updateData(map,model);
                            Log.d("chk", "unClick: ");

                        }
                    }
                });




//
        holder.textView.setText(model.getText());

    }

    public void updateData(HashMap map,Todo_model model)
    {
        DatabaseReference TodoRef = FirebaseDatabase.getInstance().getReference();
        TodoRef.child("TODO_LIST_DATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot:snapshot.getChildren())
                {
                    if (model.getEmail().equals(userSnapshot.child("email").getValue().toString())  && model.getText().equals(userSnapshot.child("text").getValue().toString()))
                    {
                        TodoRef.child("TODO_LIST_DATA").child(userSnapshot.getKey()).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    Log.d("TODO", "TODO LIST UPDATED..!!");
                                }
                                else
                                {
                                    Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @NonNull
    @Override
    public todoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_layout,parent,false);
        return new todoAdapter.ViewHolder(view);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.todo_text);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
