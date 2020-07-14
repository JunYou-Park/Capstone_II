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

import blog.practice.capstone_ii.R;

public class NameFragment extends Fragment {

    private static TextInputEditText et_name;
    private TextInputLayout inputLayout;
    public static NameFragment newInstance(){
        NameFragment fragment = new NameFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name, container, false);
        et_name = view.findViewById(R.id.et_name);
        inputLayout = view.findViewById(R.id.input_name_layout);

        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals("")){
                    inputLayout.setError("이름을 입력해주세요");
                }
                else{
                    inputLayout.setError(null);
                }
            }
        });
        return view;
    }

    public static String getText(){
        return et_name.getText().toString();
    }
}
