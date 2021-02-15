package blog.practice.capstone_ii.Email;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {

    private final String user = "junyeong@gmail.com"; // 보내는 계정의 id
    private final String password = "!qkrwnsdud12"; // 보내는 계정의 pw

    private String subject = "";
    private String content = "";

    public SendMail(String subject, String content) {
        this.subject = subject;
        this.content = content;
        Log.d("확인","Subject: " + subject + ", Content: " + content);
    }

    public void sendSecurityCode(Context context, String sendTo){
        Log.d("Check","sendSecurityCode 실행");
        try{
            GMailSender gMailSender = new GMailSender(user,password);
            gMailSender.sendMail(subject, content, sendTo);
            //Toast.makeText(context, "이메일을 성곡적으로 전송했습니다.",Toast.LENGTH_SHORT).show();
        }
        catch (SendFailedException e){
            Log.d("Check", "SendFailedException: "+e.getMessage());
            Toast.makeText(context,"이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        }
        catch (MessagingException e){
            Log.d("Check", "MessagingException: "+e.getMessage());
            Toast.makeText(context,"인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
