package blog.practice.capstone_ii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import blog.practice.capstone_ii.DataVO.UserVO;
import blog.practice.capstone_ii.OkHttpConnetion.HttpConnection;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView imgQRCode;
    private String flag = "null";
    private HttpConnection httpConnection = HttpConnection.getInstance();

    private String jsonStr, originCnt= "";
    private UserVO userVO;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private String token;
    private String num4000,num3000,num2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code);
        imgQRCode = findViewById(R.id.imgQRCode);

        sp = getSharedPreferences("UserData", MODE_PRIVATE);
        editor = sp.edit();

        loadShared();

        jsonStr = getIntent().getStringExtra("jsonStr");
        originCnt = num4000+"/"+num3000+"/"+num2000;

        Log.d("확인", "jsonStr: " + jsonStr + ", originCnt: " + originCnt);
        userVO = new UserVO();
        userVO.setToken(token);
        userVO.setName(originCnt);

        Log.d("확인" , "QRCode: " + jsonStr);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(jsonStr, BarcodeFormat.QR_CODE, 400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imgQRCode.setImageBitmap(bitmap);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "스캔해주세요." , Toast.LENGTH_SHORT).show();
        TimeThread timeThread = new TimeThread();
        Thread t = new Thread(timeThread);
        t.setDaemon(true);
        t.start();

    }

    private void loadShared() {
        token = sp.getString("token", "");
        num4000 = sp.getString("num4000","");
        num3000 = sp.getString("num3000","");
        num2000 = sp.getString("num2000","");
        Log.d("확인","token: "+token);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                Log.d("확인" , timer+"");
            }
        }
    };

    private boolean flagThread = true;
    private int timer = 180;
    class TimeThread implements Runnable{
        @Override
        public void run() {
            while(flagThread){
                handler.sendEmptyMessage(0);
                timer-=1;
                if(timer==1){
                    flagThread = false;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(timer%2==0){
                    sendData(userVO);
                }
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void sendData(final UserVO userVO){
        httpConnection.requestWebServer("QRCode.do",userVO, callback);
        Log.d("확인", "QRCode.do 실행");
    }
    // okhttp callback
    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
            Log.d("확인", "오류 발생, " + e.getMessage());
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            try{
                JSONObject object = new JSONObject(response.body().string());
                flag = object.getString("flag");
                Log.d("확인" , "flag: " + flag);
                if(response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(flag.equals("true")){
                                flagThread = false;
                                Toast.makeText(getApplicationContext(), "식권 사용 성공" , Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
                else{
                    Log.d("확인", "서버접속 실패");
                    return;
                }
            }
            catch (IOException e) {
                Log.d("Check", "IOException: " + e.getMessage());
            } catch (JSONException e) {
                Log.d("Check", "JSONException: " + e.getMessage());
            } catch (Exception e) {
                Log.d("Check", "Exception: " + e.getMessage());
            }
        }
    };

    @Override
    protected void onDestroy() {
        flagThread = false;
        super.onDestroy();
    }
}
