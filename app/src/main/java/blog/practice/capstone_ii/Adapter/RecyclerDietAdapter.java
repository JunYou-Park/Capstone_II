package blog.practice.capstone_ii.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import blog.practice.capstone_ii.DataVO.Diet;
import blog.practice.capstone_ii.MultiUseActivity;
import blog.practice.capstone_ii.R;

public class RecyclerDietAdapter extends RecyclerView.Adapter<RecyclerDietAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> listDiet;
    String TAG = getClass().toString();

    public RecyclerDietAdapter(Activity activity, ArrayList<String> listDiet) {
        this.activity = activity;
        this.listDiet = listDiet;
        Log.d("확인" ,TAG+ "생성자 생성");
    }

    @Override
    public RecyclerDietAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(activity).inflate(R.layout.list_item_layout,parent,false);
        RecyclerDietAdapter.ViewHolder viewHolder = new RecyclerDietAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerDietAdapter.ViewHolder holder, int position) {
        String day = listDiet.get(position).substring(0,listDiet.get(position).indexOf("_"));
        String diet = listDiet.get(position).substring(listDiet.get(position).indexOf("_")+1);
        holder.txtDiet.setText(diet);
        holder.txtDay.setText(day);

    }

    @Override
    public int getItemCount() {
        return listDiet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtDiet, txtDay;

        public ViewHolder(View itemView) {
            super(itemView);
            txtDiet = itemView.findViewById(R.id.txtDiet);
            txtDay = itemView.findViewById(R.id.txtDay);
        }
    }
}
