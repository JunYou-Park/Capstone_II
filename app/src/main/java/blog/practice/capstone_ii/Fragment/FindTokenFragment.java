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


public class FindTokenFragment extends Fragment {

    private TextInputLayout input_F_name_layout, input_F_phone_layout;
    private static TextInputEditText et_F_phone, et_F_name;

    public FindTokenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_token, container, false);
        et_F_phone = view.findViewById(R.id.et_FT_phone);
        et_F_name = view.findViewById(R.id.et_FT_name);
        input_F_phone_layout = view.findViewById(R.id.input_FT_phone_layout);
        input_F_name_layout = view.findViewById(R.id.input_FT_name_layout);

        et_F_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.PHONE.matcher(s).matches()){
                    input_F_phone_layout.setError("전화번호가 아닙니다.");
                }
                else{
                    input_F_phone_layout.setError(null);
                }
            }
        });

        return view;
    }

    public static String getFTPhone(){
        return et_F_phone.getText().toString();
    }
    public static String getFTName(){
        return et_F_name.getText().toString();
    }
}
