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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    Activity activity;
    private final ArrayList db_id;
    private final ArrayList db_name;
    private final ArrayList db_tag;
    private final ArrayList db_repeat;

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
    @SuppressWarnings("deprecation")
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        String repetition = String.valueOf(db_repeat.get(holder.getAdapterPosition()));
        String[] itemsSettings = repetition.split(",");
        List<String> items = new ArrayList<>(Arrays.asList(itemsSettings));

        final int itemSize = 6;
        for(int i = 0; i < itemSize; i++){
            items.remove("");
        }

        String item = String.valueOf(items);
        item = item.substring(1, item.length() - 1);

        holder.db_id_txt.setText(String.valueOf(db_id.get(holder.getAdapterPosition())));
        holder.db_name_txt.setText(String.valueOf(db_name.get(holder.getAdapterPosition())));
        holder.db_tag_txt.setText(String.valueOf(db_tag.get(holder.getAdapterPosition())));
        holder.db_repeat_txt.setText(item);

        String finalItem = item;
        holder.my_row_layout.setOnClickListener(view -> {
            Intent intent = new Intent(context, UpdateActivity.class);
            Log.d("UpdateActivity", "Adapter " + db_id);
            Log.d("UpdateActivity", "Adapter " + db_name);
            Log.d("UpdateActivity", "Adapter " + db_tag);
            Log.d("UpdateActivity", "Adapter " + db_repeat);
            intent.putExtra("id", String.valueOf(db_id.get(holder.getAdapterPosition())));
            intent.putExtra("name", String.valueOf(db_name.get(holder.getAdapterPosition())));
            intent.putExtra("tag", String.valueOf(db_tag.get(holder.getAdapterPosition())));
            intent.putExtra("repeat", finalItem);
            activity.startActivityForResult(intent, 1);
        });
    }

    @Override
    public int getItemCount() {
        return db_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;
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
            monday = itemView.findViewById(R.id.monday_button);
            tuesday = itemView.findViewById(R.id.tuesday_button);
            wednesday = itemView.findViewById(R.id.wednesday_button);
            thursday = itemView.findViewById(R.id.thursday_button);
            friday = itemView.findViewById(R.id.friday_button);
            saturday = itemView.findViewById(R.id.saturday_button);
            sunday = itemView.findViewById(R.id.sunday_button);
        }
    }
}
