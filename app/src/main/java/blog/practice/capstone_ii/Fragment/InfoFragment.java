package blog.practice.capstone_ii.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import blog.practice.capstone_ii.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class InfoFragment extends Fragment {

    TextInputEditText txtToken,txtName,txtBefore_pw,txtAfter_pw,txtPhone;
    TextInputLayout txtBefore_pw_layout, txtAfter_pw_layout,txtPhone_layout;
    Button btnChangeOK;
    String token,name,phone;
    View view_fragment;
    SharedPreferences sp;

    final String TAG = "InfoFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        sp = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        loadShared();

        txtToken = view.findViewById(R.id.txtToken);
        txtName = view.findViewById(R.id.txtName);
        txtBefore_pw = view.findViewById(R.id.txtBefore_pw);
        txtAfter_pw = view.findViewById(R.id.txtAfter_pw);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtBefore_pw_layout = view.findViewById(R.id.txtBefore_pw_layout);
        txtAfter_pw_layout = view.findViewById(R.id.txtAfter_pw_layout);
        txtPhone_layout = view.findViewById(R.id.txtPhone_layout);
        btnChangeOK = view.findViewById(R.id.btnChangeOK);

        txtToken.setText(token);
        txtName.setText(name);
        txtPhone.setText(phone);

        txtAfter_pw_layout.setCounterEnabled(true);
        txtAfter_pw_layout.setCounterMaxLength(16);
        txtAfter_pw_layout.setPasswordVisibilityToggleEnabled(true);
        txtAfter_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[`~!@$%#\\^&*\\-_\\+=\"])[A-Za-z\\d!\"$%#\\^&*!@\\`\\~_=\\+\\-\"]{8,16}$",s)){
                    txtAfter_pw_layout.setError("① 최소 8자 ~ 최대 16자 이내로 입력합니다.\n" +
                            "② 반드시 영문, 숫자, 특수문자가 각 1자리 이상 포함되어야 합니다.\n" +
                            "③ 특수문자 중 <, >, (, ) ', /, |, \\ 는 사용할수 없습니다.");
                    btnChangeOK.setEnabled(false);
                }
                else if(txtAfter_pw.getText().toString().equals(txtBefore_pw.getText().toString())){
                    txtAfter_pw_layout.setError("기존의 비밀번호와 같을 수 없습니다.");
                    btnChangeOK.setEnabled(false);
                }
                else{
                    txtAfter_pw_layout.setError(null);
                    btnChangeOK.setEnabled(true);
                }
            }
        });

        txtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!Pattern.matches("^\\d{9,11}$",s)){
                    txtPhone_layout.setError("휴대폰 번호가 아닙니다.(주의 본인의 휴대전화가 아니면 이메일 찾기가 불가능 합니다.)");
                }
                else{
                    txtPhone_layout.setError(null);
                }
            }
        });

        txtBefore_pw_layout.setCounterEnabled(true);
        txtBefore_pw_layout.setCounterMaxLength(16);
        txtBefore_pw_layout.setPasswordVisibilityToggleEnabled(true);

        view_fragment = view;

        btnChangeOK.setOnClickListener(v->{
            String afterPW = txtAfter_pw.getText().toString();
            String beforePW = txtBefore_pw.getText().toString();
            String phone = txtPhone.getText().toString();

            if(!afterPW.isEmpty()&&!beforePW.isEmpty()&&!phone.isEmpty()){
                UserVO userVO = new UserVO(token,beforePW,afterPW,phone);
                sendData(userVO);
                // 0.3초 정도 멈추고 getFlag상태를 확인해야 확인이 가능하다.
                try{
                    Thread.sleep(300);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                // callback 안에 넣으면 실행이 안된다.
                if(getFlag.equals("true")){
                    Toast.makeText(getContext(), "유저 정보 변경", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view_fragment).navigate(R.id.action_nav_info_to_nav_home);
                }
                else{
                    Toast.makeText(getContext(), "기존의 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
           else {
                Toast.makeText(getContext(),"모든 데이터를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    /*쉐어드값 불러오기*/
    private void loadShared() {
        token = sp.getString("token", "");
        name = sp.getString("dName", "");
        phone = sp.getString("dPhone", "");
        Log.d("확인","token: "+ token + ", name: "+ name + ", phone: "+ phone);
    }

    private HttpConnection httpConnection = HttpConnection.getInstance();
    private void sendData(final UserVO userVO){
        httpConnection.requestWebServer("ChangeUserInfo.do",userVO, info_callback);
    }
    String getFlag = "false";
    // okhttp callback
    private Callback info_callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("확인", "오류 발생, " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            if(response.isSuccessful()){
                try{
                    JSONObject object = new JSONObject(response.body().string());
                    getFlag = object.getString("flag");
                    Log.d("확인", TAG + " flag:" + getFlag);
                }
                catch (IOException e) {
                    Log.d("Check", "IOException: " + e.getMessage());
                } catch (JSONException e) {
                    Log.d("Check", "JSONException: " + e.getMessage());
                } catch (Exception e) {
                    Log.d("Check", "Exception: " + e.getMessage());
                }
            }
            else{
                Log.d("확인", "response 오류 발생");
            }

        }
    };


}