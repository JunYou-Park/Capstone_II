package blog.practice.capstone_ii.Fragment;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import blog.practice.capstone_ii.Adapter.RecyclerAdapter;
import blog.practice.capstone_ii.DataVO.Diet;
import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.Interface.OnItemClickListener;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import blog.practice.capstone_ii.QRCodeActivity;
import blog.practice.capstone_ii.R;
import blog.practice.capstone_ii.Util.ItemDecoration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView txt4000,txt3000,txt2000;
    private HttpConnection httpConnection = HttpConnection.getInstance();
    private String token;
    private final String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sp = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        editor = sp.edit();

        txt4000 = view.findViewById(R.id.txt4000);
        txt3000 = view.findViewById(R.id.txt3000);
        txt2000 = view.findViewById(R.id.txt2000);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecoration(getActivity()));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getActivity(), getDietList());

        recyclerView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Diet diet) {
                String jsonStr = submitTicket(diet);
                if(!jsonStr.equals("")){
                    Intent intent = new Intent(getActivity(), QRCodeActivity.class);
                    intent.putExtra("jsonStr",jsonStr);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity(), diet.getdPrice()+" 식권이 부족합니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // 선택한 메뉴의 데이터를 저장
        initShared();

        // token 값을 불러온다.
        loadShared();
        UserVO userVO = new UserVO();
        userVO.setToken(token);

        //티켓 갯수를 불러온다.
        sendData(userVO);

        return view;
    }

    private String submitTicket(Diet diet){
        int ticketcnt = 0;
        switch (diet.getdPrice()){
            case "4000":
                ticketcnt = Integer.parseInt(txt4000.getText().toString());
                if(ticketcnt>0){
                    String jsonStr = token+ "/" + diet.getdId()+" num" + diet.getdPrice()+" 1";
                    return jsonStr;
                }
                else{
                    return "";
                }

            case "3000":
                ticketcnt = Integer.parseInt(txt3000.getText().toString());
                if(ticketcnt>0){
                    String jsonStr = token+ "/" + diet.getdId()+" num" + diet.getdPrice()+" 1";
                    return jsonStr;
                }
                else{
                    return "";
                }

            case "2000":
                ticketcnt = Integer.parseInt(txt2000.getText().toString());
                if(ticketcnt>0){
                    String jsonStr = token+ "/" + diet.getdId()+" num" + diet.getdPrice()+" 1";
                    return jsonStr;
                }
                else{
                    return "";
                }
            default:
                return "";
        }
    }

    private void initShared(){
        editor.putString("jsonStr", "");
        editor.apply();
    }

    private void loadShared() {
        token = sp.getString("token", "");
        Log.d("확인","token: "+ token);
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
            InputStream is = getActivity().getAssets().open("diet.json");
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
                Log.d("확인", diet.toString());
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDiet;
    }

    // 티켓 장 수
    private String num4000,num3000,num2000;
    // 유저 데이터 값 ( InfoFragment 에서 사용 )
    private String dName, dPhone;
    private void sendData(final UserVO userVO){
        httpConnection.requestWebServer("ticketList.do",userVO, ticket_callback);
    }
    // okhttp callback
    private Callback ticket_callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("확인", TAG + "오류 발생, " + e.getMessage());
        }
        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try{
                JSONObject object = new JSONObject(response.body().string());
                num4000 = object.getString("num4000");
                num3000 = object.getString("num3000");
                num2000 = object.getString("num2000");
                dName = object.getString("dName");
                dPhone = object.getString("dPhone");

                Log.d("확인",  "dName: " + dName + ", dPhone: " + dPhone + ", num4000:" + num4000 + ", num3000: " + num3000 + ", num2000: " + num2000);
                if(response.isSuccessful()){
                    txt4000.setText(num4000);
                    txt3000.setText(num3000);
                    txt2000.setText(num2000);

                    editor.putString("num4000", num4000);
                    editor.putString("num3000", num3000);
                    editor.putString("num2000", num2000);
                    editor.putString("dName", dName);
                    editor.putString("dPhone", dPhone);
                    editor.apply();
                }
                else{
                    Log.d("확인", "서버접속 실패");
                    return;
                }
            }
            catch (IOException e) {
                Log.d("Check", "IOException: " + e.getMessage());
            } catch (JSONException e) {
                Log.d("Check", "JSONException: " + e.getMessage());
            } catch (Exception e) {
                Log.d("Check", "Exception: " + e.getMessage());
            }
        }
    };

}