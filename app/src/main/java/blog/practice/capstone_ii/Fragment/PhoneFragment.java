package blog.practice.capstone_ii.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import blog.practice.capstone_ii.R;

public class PhoneFragment extends Fragment {
    private TextInputLayout inputLayout;
    private static TextInputEditText et_phone;

    public static PhoneFragment newInstance(){
        PhoneFragment fragment = new PhoneFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        et_phone = view.findViewById(R.id.et_phone);
        inputLayout = view.findViewById(R.id.input_phone_layout);

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!Pattern.matches("^\\d{9,11}$",s)){
                    inputLayout.setError("휴대폰 번호가 아닙니다.(주의 본인의 휴대전화가 아니면 이메일 찾기가 불가능 합니다.)");
                }
                else{
                    inputLayout.setError(null);
                }
            }
        });
        return view;
    }

    public static String getText(){
        return et_phone.getText().toString();
    }
}
