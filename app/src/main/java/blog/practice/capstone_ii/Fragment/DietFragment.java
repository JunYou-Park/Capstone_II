package blog.practice.capstone_ii.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import blog.practice.capstone_ii.Adapter.RecyclerDietAdapter;
import blog.practice.capstone_ii.DataVO.Diet;
import blog.practice.capstone_ii.R;
import blog.practice.capstone_ii.Util.ItemDecoration;

import static blog.practice.capstone_ii.Util.StringUtility.return_arr;

public class DietFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_diet, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_diet);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecoration(getActivity()));
        RecyclerDietAdapter recyclerAdapter = new RecyclerDietAdapter(getActivity(), getDietList());

        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

    private ArrayList<String> getDietList() {
        // json에 날짜별로 저장되어있기 때문에 오늘 날짜를 알아야 한다.
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(date);

        System.out.println(dateStr);
        String [] arrStr = return_arr(dateStr,"-");

        Calendar cal = Calendar.getInstance();
        int year = Integer.parseInt(arrStr[0]);
        int month = Integer.parseInt(arrStr[1]);
        int today = Integer.parseInt(arrStr[2]);
        cal.set(year,month,today);

        if(month!=0) {
            cal.set(year,month-1,today);
        }
        else {
            cal.set(year,month,today);
        }
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        if(lastDay - today > 8){
            lastDay = today + 7;
        }

        ArrayList<String> list = new ArrayList<>();
        Gson gson = new Gson();
        for(int i = today; i <= lastDay; i++) {
            try {
                InputStream is = getActivity().getAssets().open("diet.json");
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();
                String json = new String(buffer, "UTF-8");
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray(i+"");
                int index = 0;
                String content = i+"_";
                while (index < jsonArray.length()) {
                    Diet diet = gson.fromJson(jsonArray.get(index).toString(), Diet.class);
                    content += diet.getdName()+",";
                    Log.d("확인", diet.toString());
                    index++;
                }
                content = content.substring(0,content.lastIndexOf(","));
                // 날짜와 식단의 이름만 저장
                list.add(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return list;
    }
}