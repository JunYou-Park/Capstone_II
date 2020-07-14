package blog.practice.capstone_ii.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import blog.practice.capstone_ii.R;


public class EmailFragment extends Fragment {

    private TextInputLayout inputLayout;
    private static TextInputEditText et_email;

    public static EmailFragment newInstance(){
        EmailFragment fragment = new EmailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);
        et_email = view.findViewById(R.id.et_email);
        inputLayout = view.findViewById(R.id.input_email_layout);

        et_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    inputLayout.setError("이메일 주소가 아닙니다.");
                }
                else{
                    inputLayout.setError(null);
                }
            }
        });
        return view;
    }

    public static String getText(){
        return et_email.getText().toString();
    }
}
