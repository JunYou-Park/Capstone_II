package blog.practice.capstone_ii.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import blog.practice.capstone_ii.R;


public class FindPasswordFragment extends Fragment {

    private TextInputLayout input_F_token_layout, input_F_name_layout;
    private static TextInputEditText et_F_token, et_F_name;

    public FindPasswordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_password, container, false);
        et_F_token = view.findViewById(R.id.et_FP_token);
        et_F_name = view.findViewById(R.id.et_FP_name);
        input_F_token_layout = view.findViewById(R.id.input_FP_token_layout);
        input_F_name_layout = view.findViewById(R.id.input_FP_name_layout);

        et_F_token.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    input_F_token_layout.setError("이메일 주소가 아닙니다.");
                }
                else{
                    input_F_token_layout.setError(null);
                }
            }
        });
        return view;
    }
    public static String getFPToken(){
        return et_F_token.getText().toString();
    }
    public static String getFPName(){
        return et_F_name.getText().toString();
    }
}
