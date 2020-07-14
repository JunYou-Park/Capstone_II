package blog.practice.capstone_ii.Adapter;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import blog.practice.capstone_ii.DataVO.Diet;
import blog.practice.capstone_ii.Interface.OnItemClickListener;
import blog.practice.capstone_ii.MultiUseActivity;
import blog.practice.capstone_ii.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Diet> listDiet;
    OnItemClickListener onItemClickListener = null;
    String TAG = getClass().toString();


    public void setOnItemClickListener(OnItemClickListener listener){
        onItemClickListener = listener;
    }

    public RecyclerAdapter(Activity activity, ArrayList<Diet> listDiet) {
        this.activity = activity;
        this.listDiet = listDiet;
        Log.d("확인" ,TAG+ "생성자 생성");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(activity).inflate(R.layout.list_diet_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Diet diet = listDiet.get(position);
        Log.d("확인", TAG+"  " + diet.getdThumb());
        Glide.with(activity)
                .load(diet.getdThumb())
                .thumbnail(0.5f)
                .into(holder.imgDiet);

        holder.txtName.setText(diet.getdName());
        holder.txtPrice.setText(diet.getdPrice());

        holder.cardViewDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, diet);
            }
        });

        holder.cardViewDiet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(activity, MultiUseActivity.class);
                activity.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDiet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice;
        ImageView imgDiet;
        CardView cardViewDiet;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgDiet = itemView.findViewById(R.id.imgDiet);
            cardViewDiet = itemView.findViewById(R.id.cardViewDiet);
        }
    }
}
