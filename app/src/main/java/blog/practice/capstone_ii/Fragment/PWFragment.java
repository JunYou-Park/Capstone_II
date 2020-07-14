package blog.practice.capstone_ii.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import blog.practice.capstone_ii.R;

public class PWFragment extends Fragment {

    private static TextInputEditText et_pw;
    private TextInputLayout inputLayout;
    public static PWFragment newInstance(){
        PWFragment fragment = new PWFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pw, container, false);
        et_pw = view.findViewById(R.id.et_pw);
        inputLayout = view.findViewById(R.id.input_pw_layout);
        inputLayout.setCounterEnabled(true);
        inputLayout.setCounterMaxLength(16);
        inputLayout.setPasswordVisibilityToggleEnabled(true);
        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[`~!@$%#\\^&*\\-_\\+=\"])[A-Za-z\\d!\"$%#\\^&*!@\\`\\~_=\\+\\-\"]{8,16}$",s)){
                    inputLayout.setError("① 최소 8자 ~ 최대 16자 이내로 입력합니다.\n" +
                            "② 반드시 영문, 숫자, 특수문자가 각 1자리 이상 포함되어야 합니다.\n" +
                            "③ 특수문자 중 <, >, (, ) ', /, |, \\ 는 사용할수 없습니다.");
                }
                else{
                    inputLayout.setError(null);
                }
            }
        });
        return view;
    }

    public static String getText(){
        return et_pw.getText().toString();
    }
}
