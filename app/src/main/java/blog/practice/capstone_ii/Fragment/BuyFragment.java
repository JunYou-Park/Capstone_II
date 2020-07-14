package blog.practice.capstone_ii.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import blog.practice.capstone_ii.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class BuyFragment extends Fragment {

    private ImageView img_4000_plus, img_4000_minus, img_3000_plus, img_3000_minus, img_2000_plus, img_2000_minus;
    private CheckBox check_4000, check_3000, check_2000;
    private TextView txt_4000_num, txt_3000_num, txt_2000_num, txt_totalPrice;
    private int totalPrice = 0;
    private Button btn_buy;

    private SharedPreferences sp;
    private String token;
    private View view_fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy, container, false);
        sp = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        loadShared();

        img_4000_plus = view.findViewById(R.id.img_plus);
        img_4000_minus = view.findViewById(R.id.img_minus);
        img_3000_plus = view.findViewById(R.id.img_3000_plus);
        img_3000_minus = view.findViewById(R.id.img_3000_minus);
        img_2000_plus = view.findViewById(R.id.img_2000_plus);
        img_2000_minus = view.findViewById(R.id.img_2000_minus);
        check_4000 = view.findViewById(R.id.check_4000);
        check_3000 = view.findViewById(R.id.check_3000);
        check_2000 = view.findViewById(R.id.check_2000);
        txt_4000_num = view.findViewById(R.id.txt_num);
        txt_3000_num = view.findViewById(R.id.txt_3000_num);
        txt_2000_num = view.findViewById(R.id.txt_2000_num);
        txt_totalPrice = view.findViewById(R.id.txt_totalPrice);
        btn_buy = view.findViewById(R.id.btn_buy);

        img_4000_plus.setEnabled(false);
        img_4000_minus.setEnabled(false);
        img_3000_plus.setEnabled(false);
        img_3000_minus.setEnabled(false);
        img_2000_plus.setEnabled(false);
        img_2000_minus.setEnabled(false);

        img_4000_plus.setOnClickListener(v -> {
            int temp = Integer.parseInt(txt_4000_num.getText() + "") + 1;
            Log.d("체킹", temp + "");
            txt_4000_num.setText(temp + "");
            txt_totalPrice.setText(returnTotalPrice(0, 4000) + "");
        });

        img_4000_minus.setOnClickListener(v -> {
            int temp = Integer.parseInt(txt_4000_num.getText() + "") - 1;
            if (temp >= 0) {
                Log.d("체킹", temp + "");
                txt_4000_num.setText(temp + "");
                txt_totalPrice.setText(returnTotalPrice(1, 4000) + "");
            } else {
                Toast.makeText(getContext(), "식권을 0장 이상 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        img_3000_plus.setOnClickListener(v -> {
            int temp = Integer.parseInt(txt_3000_num.getText() + "") + 1;
            Log.d("체킹", temp + "");
            txt_3000_num.setText(temp + "");
            txt_totalPrice.setText(returnTotalPrice(0, 3000) + "");
        });

        img_3000_minus.setOnClickListener(v -> {
            int temp = Integer.parseInt(txt_3000_num.getText() + "") - 1;
            if (temp >= 0) {
                Log.d("체킹", temp + "");
                txt_3000_num.setText(temp + "");
                txt_totalPrice.setText(returnTotalPrice(1, 3000) + "");
            } else {
                Toast.makeText(getContext(), "식권을 0장 이상 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        img_2000_plus.setOnClickListener(v -> {
            int temp = Integer.parseInt(txt_2000_num.getText() + "") + 1;
            Log.d("체킹", temp + "");
            txt_2000_num.setText(temp + "");
            txt_totalPrice.setText(returnTotalPrice(0, 2000) + "");
        });

        img_2000_minus.setOnClickListener(v -> {
            int temp = Integer.parseInt(txt_2000_num.getText() + "") - 1;
            if (temp >= 0) {
                Log.d("체킹", temp + "");
                txt_2000_num.setText(temp + "");
                txt_totalPrice.setText(returnTotalPrice(1, 2000) + "");
            } else {
                Toast.makeText(getContext(), "식권을 0장 이상 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        check_4000.setOnClickListener(v -> {
            if (check_4000.isChecked()) {
                img_4000_plus.setEnabled(true);
                img_4000_minus.setEnabled(true);
            }
            if (!check_4000.isChecked()) {
                img_4000_plus.setEnabled(false);
                img_4000_minus.setEnabled(false);
                txt_totalPrice.setText(returnTotalPrice(2, 4000) + "");
                txt_4000_num.setText(0 + "");
            }
        });

        check_3000.setOnClickListener(v -> {
            if (check_3000.isChecked()) {
                img_3000_plus.setEnabled(true);
                img_3000_minus.setEnabled(true);
            }
            if (!check_3000.isChecked()) {
                img_3000_plus.setEnabled(false);
                img_3000_minus.setEnabled(false);
                txt_totalPrice.setText(returnTotalPrice(2, 3000) + "");
                txt_3000_num.setText(0 + "");
            }
        });

        check_2000.setOnClickListener(v -> {
            if (check_2000.isChecked()) {
                img_2000_plus.setEnabled(true);
                img_2000_minus.setEnabled(true);
            }
            if (!check_2000.isChecked()) {
                img_2000_plus.setEnabled(false);
                img_2000_minus.setEnabled(false);
                txt_totalPrice.setText(returnTotalPrice(2, 2000) + "");
                txt_2000_num.setText(0 + "");
            }
        });

        btn_buy.setOnClickListener(v -> {
            int num4000 = Integer.parseInt(txt_4000_num.getText().toString());
            int num3000 = Integer.parseInt(txt_3000_num.getText().toString());
            int num2000 = Integer.parseInt(txt_2000_num.getText().toString());
            UserVO userVO = new UserVO(token, num4000, num3000, num2000);
            sendData(userVO);
            // 0.3초 정도 멈추고 getFlag상태를 확인해야 확인이 가능하다.
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // callback 안에 넣으면 실행이 안된다.
            if (getFlag.equals("true")) {
                Toast.makeText(getContext(), "구매성공", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view_fragment).navigate(R.id.action_nav_buy_to_nav_home);
            } else {
                Toast.makeText(getContext(), "구매실패", Toast.LENGTH_SHORT).show();
            }
        });

        view_fragment = view;
        return view;
    }

    /*쉐어드값 불러오기*/
    private void loadShared() {
        token = sp.getString("token", "");
        Log.d("확인", "token: " + token);
    }


    private int returnTotalPrice(int option, int price) {
        if (option == 0) { // 증가
            totalPrice += price;
        } else if (option == 1) { // 감소
            totalPrice -= price;
        } else if (option == 2) { // 선택해제
            switch (price) {
                case 4000:
                    totalPrice -= price * Integer.parseInt(txt_4000_num.getText() + "");
                    break;
                case 3000:
                    totalPrice -= price * Integer.parseInt(txt_3000_num.getText() + "");
                    break;
                case 2000:
                    totalPrice -= price * Integer.parseInt(txt_2000_num.getText() + "");
                    break;
            }

        }
        return totalPrice;
    }

    private HttpConnection httpConnection = HttpConnection.getInstance();

    public void sendData(final UserVO userVO) {
        new Thread() {
            @Override
            public void run() {
                httpConnection.requestWebServer("buyTicket.do", userVO, buy_callback);
            }
        }.start();
    }

    private String getFlag = "false";
    private Callback buy_callback = new Callback() {
        @Override
        public void onFailure( Call call, IOException e) {
            Log.d("확인", "오류 발생, " + e.getMessage());
        }

        @Override
        public void onResponse(Call call,  Response response) throws IOException {
            if (response.isSuccessful()) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    Log.d("확인", "object: " + object.toString());
                    getFlag = object.getString("flag");
                    Log.d("확인", "getFlag: " + getFlag);
                } catch (JSONException e) {
                    Log.d("확인", "JSONException: " + e.getMessage());
                } catch (Exception e) {
                    Log.d("확인", "Exception: " + e.getMessage());
                }
            } else {
                Log.d("확인", "서버접속 실패");
                return;
            }
        }
    };
}