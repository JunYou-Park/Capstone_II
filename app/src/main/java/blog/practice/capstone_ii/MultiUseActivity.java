package blog.practice.capstone_ii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import blog.practice.capstone_ii.Adapter.RecyclerMultiAdapter;
import blog.practice.capstone_ii.DataVO.Diet;

public class MultiUseActivity extends AppCompatActivity {

    private RecyclerView recyclerMulti;
    private RecyclerMultiAdapter recyclerMultiAdapter;
    private Button btn_createQRCode;
    private SharedPreferences sp;

    private final String TAG = getClass().toString();

    private String num4000,num3000,num2000;
    private TextView txt_num_4000, txt_num_3000, txt_num_2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_use);

        txt_num_4000 = findViewById(R.id.txt_num_4000);
        txt_num_3000 = findViewById(R.id.txt_num_3000);
        txt_num_2000 = findViewById(R.id.txt_num_2000);

        sp = getSharedPreferences("UserData",MODE_PRIVATE);
        loadShared();

        btn_createQRCode = findViewById(R.id.btn_createQRCode);
        recyclerMulti = findViewById(R.id.recyclerMulti);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMulti.setLayoutManager(layoutManager);
        recyclerMultiAdapter = new RecyclerMultiAdapter(this, getDietList(),num4000,num3000,num2000);
        recyclerMulti.setAdapter(recyclerMultiAdapter);

        btn_createQRCode.setOnClickListener(v->{
            // 해시맵으로 값 받아오기
            HashMap<String,String> hashMap = recyclerMultiAdapter.getHashMap();
            String jsonStr = "";
            for(String key: hashMap.keySet()){
                // ,으로 속성을 나누고 /로 메뉴를 나눈다.
                jsonStr += key+" "+hashMap.get(key)+"/";
            }
            jsonStr = token+"/"+jsonStr;
            Log.d("확인", jsonStr);
            Intent intent = new Intent(this, QRCodeActivity.class);
            intent.putExtra("jsonStr", jsonStr);
            startActivity(intent);
        });
    }
    private String token;
    private void loadShared() {
        token = sp.getString("token","");
        num4000 = sp.getString("num4000","");
        num3000 = sp.getString("num3000","");
        num2000 = sp.getString("num2000","");
        Log.d("확인", TAG +num4000+ ", " +num3000+", "+num2000);
        txt_num_4000.setText(num4000);
        txt_num_3000.setText(num3000);
        txt_num_2000.setText(num2000);
    }

    private ArrayList<Diet> getDietList() {
        // json에 날짜별로 저장되어있기 때문에 오늘 날짜를 알아야 한다.
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd");
        int today = Integer.parseInt(format.format(date));
        Log.d("확인", "today: " + today);
        ArrayList<Diet> listDiet = new ArrayList<>();
        Gson gson = new Gson();


        try {
            InputStream is = getAssets().open("diet.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(today+"");
            int index = 0;
            while (index < jsonArray.length()) {
                Diet diet = gson.fromJson(jsonArray.get(index).toString(), Diet.class);
                listDiet.add(diet);
                Log.d("확인", diet.getdId()+", " + diet.getdName()+", " + diet.getdPrice()+", " + diet.getdThumb());
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDiet;
    }
}
