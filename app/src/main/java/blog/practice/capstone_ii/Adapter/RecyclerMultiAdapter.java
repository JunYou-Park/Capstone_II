package blog.practice.capstone_ii.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import blog.practice.capstone_ii.DataVO.Diet;
import blog.practice.capstone_ii.Interface.OnItemClickListener;
import blog.practice.capstone_ii.R;

public class RecyclerMultiAdapter extends RecyclerView.Adapter<RecyclerMultiAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<Diet> listDiet;
    private OnItemClickListener onItemClickListener = null;
    private String TAG = getClass().toString();
    private static HashMap<String, String> hashMap = new HashMap<>();
    private int num4000,num3000,num2000, temp4000,temp3000,temp2000;
   public void setOnItemClickListener(OnItemClickListener listener){
       onItemClickListener = listener;
   }

    public RecyclerMultiAdapter(Activity activity, ArrayList<Diet> listDiet, String num4000, String num3000, String num2000) {
        this.activity = activity;
        this.listDiet = listDiet;
        this.num4000 = Integer.parseInt(num4000);
        this.num3000 = Integer.parseInt(num3000);
        this.num2000 = Integer.parseInt(num2000);
        temp4000 = this.num4000;
        temp3000 = this.num3000;
        temp2000 = this.num2000;

        // static 이라 hashMap을 초기화해주지 않으면 key값과 value가 계속 존재하기 때문에 key값의 순서가 섞이게 된다.
        removeHash(hashMap);

    }

    // 해시맵 삭제 메서드
    private void removeHash(HashMap<String, String> hash) {
        Iterator<String> it = hash.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            final String value = hash.get(key);
            if(value == null) {
                Log.d("확인",hash.get(key) + ", value 없음");
            }
            it.remove();
        }

        if(hash.size()>0) {
            printHash(hash);
        }
        else {
            Log.d("확인","해시 없음");
        }
    }

    private void printHash(HashMap<String,String> hash) {
        for(String key : hash.keySet()) {
            Log.d("확인","key: " + key + ", value: " + hash.get(key));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       Context context = parent.getContext();
       LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View view = inflater.inflate(R.layout.list_multi_diet,parent,false);

        RecyclerMultiAdapter.ViewHolder viewHolder = new RecyclerMultiAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Diet diet = listDiet.get(position);
        Log.d("확인", position+"");
        Log.d("확인", TAG+"  " + diet.getdThumb());
        Glide.with(activity)
                .load(diet.getdThumb())
                .thumbnail(0.5f)
                .into(holder.img_menu);

        holder.txt_menu_name.setText(diet.getdName());
        holder.txt_menu_price.setText(diet.getdPrice());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    holder.txt_num.setText("0");
                    hashMap.remove(diet.getdId());
                }
            }
        });
        String ticket_type = "num" +diet.getdPrice();
        Log.d("확인", TAG + ": " + ticket_type);
        holder.img_plus.setOnClickListener(v->{
            if(holder.checkBox.isChecked()) {
                int temp = Integer.parseInt(holder.txt_num.getText() + "") + 1;
                if(check_ticket_type(ticket_type, 0)){
                    holder.txt_num.setText(temp + "");
                    hashMap.put(diet.getdId(),ticket_type+" " +temp);
                }
                else{
                    Toast.makeText(activity, "보유하신 식권의 수량이 초과되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.img_minus.setOnClickListener(v->{
            if(holder.checkBox.isChecked()) {
                int temp = Integer.parseInt(holder.txt_num.getText() + "") - 1;
                if (temp >= 0) {
                    holder.txt_num.setText(temp + "");
                    check_ticket_type(ticket_type,1);
                    if(temp==0){
                        hashMap.remove(diet.getdId());
                    }
                    else{
                        hashMap.put(diet.getdId(),ticket_type+" " +temp);
                    }
                }
            }
        });

    }

    // 사용할 식권의 수량을 보유중인지 boolean 값을 반환
    public boolean check_ticket_type(String ticket_type, int option){
        switch (ticket_type){
            case "num4000":
                // 메뉴에서 +를 눌렀을 경우
                if(option==0){
                    // temp4000-1(사용하려는 식권의 수);
                    temp4000--;

                    if(temp4000 <0){
                        temp4000=0;
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                // 메뉴에서 -를 눌렸을 경우
                else{
                    // temp4000+1(사용을 취소한 식권의 수)
                    temp4000++;
                    if(temp4000>num4000){
                        temp4000--;
                        return false;
                    }
                    else{
                        return true;
                    }
                }

            case "num3000":
                // 메뉴에서 +를 눌렀을 경우
                if(option==0){
                    // temp3000-1(사용하려는 식권의 수);
                    temp3000--;
                    if(temp3000 <0){
                        temp3000=0;
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                // 메뉴에서 -를 눌렸을 경우
                else{
                    // temp3000+1(사용을 취소한 식권의 수)
                    temp3000++;
                    if(temp3000>num3000){
                        temp3000--;
                        return false;
                    }
                    else{
                        return true;
                    }
                }
            case "num2000":
                // 메뉴에서 +를 눌렀을 경우
                if(option==0){
                    // temp2000-1(사용하려는 식권의 수);
                    temp2000--;

                    if(temp2000 <0){
                        temp2000=0;
                        return false;
                    }
                    else{
                        return true;
                    }
                }
                // 메뉴에서 -를 눌렸을 경우
                else{
                    // temp2000+1(사용을 취소한 식권의 수)
                    temp2000++;
                    if(temp2000>num2000){
                        temp2000--;
                        return false;
                    }
                    else{
                        return true;
                    }
                }
            default:
                return false;
        }
    }

    @Override
    public int getItemCount() {
        return listDiet.size();
    }

    public static HashMap<String,String> getHashMap(){
       return hashMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txt_menu_name, txt_menu_price,txt_num;
        ImageView img_menu,img_plus,img_minus;
        ConstraintLayout layoutMulti;
        Button btn_createQRCode;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_menu_name = itemView.findViewById(R.id.txt_menu_name);
            txt_menu_price = itemView.findViewById(R.id.txt_menu_price);
            img_menu = itemView.findViewById(R.id.img_menu);
            img_plus = itemView.findViewById(R.id.img_plus);
            img_minus = itemView.findViewById(R.id.img_minus);
            txt_num = itemView.findViewById(R.id.txt_num);
            layoutMulti = itemView.findViewById(R.id.layoutMulti);
            btn_createQRCode = itemView.findViewById(R.id.btn_createQRCode);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
