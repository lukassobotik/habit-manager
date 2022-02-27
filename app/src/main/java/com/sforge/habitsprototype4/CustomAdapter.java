package com.sforge.habitsprototype4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    private ArrayList db_id, db_name, db_tag, db_repeat;

    Animation translate_anim;

    CustomAdapter(Context context, Activity activity, ArrayList db_id, ArrayList db_name, ArrayList db_tag, ArrayList db_repeat){
        this.context = context;
        this.activity = activity;
        this.db_id = db_id;
        this.db_name = db_name;
        this.db_tag = db_tag;
        this.db_repeat = db_repeat;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.db_id_txt.setText(String.valueOf(db_id.get(holder.getAdapterPosition())));
        holder.db_name_txt.setText(String.valueOf(db_name.get(holder.getAdapterPosition())));
        holder.db_tag_txt.setText(String.valueOf(db_tag.get(holder.getAdapterPosition())));
        holder.db_repeat_txt.setText(String.valueOf(db_repeat.get(holder.getAdapterPosition())));
        holder.my_row_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                Log.d("UpdateActivity", "Adapter " + String.valueOf(db_id));
                Log.d("UpdateActivity", "Adapter " + String.valueOf(db_name));
                Log.d("UpdateActivity", "Adapter " + String.valueOf(db_tag));
                Log.d("UpdateActivity", "Adapter " + String.valueOf(db_repeat));
                intent.putExtra("id", String.valueOf(db_id.get(holder.getAdapterPosition())));
                intent.putExtra("name", String.valueOf(db_name.get(holder.getAdapterPosition())));
                intent.putExtra("tag", String.valueOf(db_tag.get(holder.getAdapterPosition())));
                intent.putExtra("repeat", String.valueOf(db_repeat.get(holder.getAdapterPosition())));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return db_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView db_id_txt, db_name_txt, db_tag_txt, db_repeat_txt;
        LinearLayout my_row_layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            db_id_txt = itemView.findViewById(R.id.db_id_txt);
            db_name_txt = itemView.findViewById(R.id.db_name_txt);
            db_tag_txt = itemView.findViewById(R.id.db_tag_txt);
            db_repeat_txt = itemView.findViewById(R.id.db_repeat_txt);
            my_row_layout = itemView.findViewById(R.id.my_row_layout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            my_row_layout.setAnimation(translate_anim);
        }
    }
}
