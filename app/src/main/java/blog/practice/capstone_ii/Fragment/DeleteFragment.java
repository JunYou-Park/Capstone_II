package blog.practice.capstone_ii.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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

import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.LoginActivity;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import blog.practice.capstone_ii.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class DeleteFragment extends Fragment {

    private Button btn_delete;
    private TextInputLayout et_pw_layout;
    private TextInputEditText et_pw;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String pw = "";
    private String token = "";
    private final String TAG = "DeleteFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);
        et_pw_layout = view.findViewById(R.id.input_pw_layout);
        et_pw_layout.setCounterEnabled(true);
        et_pw_layout.setCounterMaxLength(16);
        et_pw_layout.setPasswordVisibilityToggleEnabled(true);


        et_pw = view.findViewById(R.id.et_pw);

        sp = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        editor = sp.edit();
        loadShared();

        btn_delete = view.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(v->{
            pw = et_pw.getText().toString();
            if(!pw.isEmpty()){
                showDialog();
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
        Log.d("확인","token: "+ token);
    }
    /*쉐어드에 입력값 저장*/
    private void saveShared(String id, String pw) {
        editor.putString("token", id);
        editor.putString("pw", pw);
        editor.apply();
    }


    private HttpConnection httpConnection = HttpConnection.getInstance();

    private void sendData(final UserVO userVO){
        httpConnection.requestWebServer("DeleteUser.do",userVO, callback);
    }
    private String getFlag = "false";
    // okhttp callback
    private Callback callback = new Callback() {
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

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("회원탈퇴");
        builder.setMessage("예를 누르면 회원 정보가 삭제됩니다.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        UserVO userVO = new UserVO();
                        userVO.setToken(token);
                        userVO.setPw(pw);
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
                            Toast.makeText(getContext(), "회원탈퇴", Toast.LENGTH_SHORT).show();
                            saveShared("","");
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        else{
                            et_pw_layout.setError("비밀번호를 확인해주세요.");
                        }
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}