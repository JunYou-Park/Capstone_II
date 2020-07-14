package blog.practice.capstone_ii.OkHttpConnetion;

import android.util.Log;

import blog.practice.capstone_ii.DataVO.UserVO;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnection {
    private final String TAG = "HttpConnection";
    private OkHttpClient client = null;
    private static HttpConnection instance = new HttpConnection();

    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection(){this.client=new OkHttpClient();}

    public void  requestWebServer(String url, UserVO userVO, Callback callback){
        Log.d("확인" , TAG + "  requestWebServer생성: " + url);
        // TODO 중복이 많음(코드 간소화 필요)
        if(url.equals("login.do")){
            RequestBody body = new FormBody.Builder()
                    .add("token",userVO.getToken())
                    .add("pw",userVO.getPw())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }

        else if(url.equals("register.do")){
            RequestBody body = new FormBody.Builder()
                    .add("type",userVO.getType())
                    .add("token",userVO.getToken())
                    .add("pw",userVO.getPw())
                    .add("name",userVO.getName())
                    .add("phone",userVO.getPhone())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }

        else if(url.equals("easy_login.do")){
            RequestBody body = new FormBody.Builder()
                    .add("type",userVO.getType())
                    .add("token",userVO.getToken())
                    .add("name",userVO.getName())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }

        else if(url.equals("ticketList.do")){
            RequestBody body = new FormBody.Builder()
                    .add("token",userVO.getToken())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        else if(url.equals("buyTicket.do")){
            RequestBody body = new FormBody.Builder()
                    .add("token",userVO.getToken())
                    .add("num4000", userVO.getNum4000()+"")
                    .add("num3000", userVO.getNum3000()+"")
                    .add("num2000", userVO.getNum2000()+"")
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        else if(url.equals("findPW.do")){
            RequestBody body = new FormBody.Builder()
                    .add("name",userVO.getName())
                    .add("token", userVO.getToken())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        else if(url.equals("findToken.do")){
            RequestBody body = new FormBody.Builder()
                    .add("name",userVO.getName())
                    .add("phone", userVO.getPhone())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        else if(url.equals("QRCode.do")){
            RequestBody body = new FormBody.Builder()
                    .add("token",userVO.getToken())
                    .add("originCnt", userVO.getName())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        else if(url.equals("ChangeUserInfo.do")){
            RequestBody body = new FormBody.Builder()
                    .add("token",userVO.getToken())
                    .add("before_pw", userVO.getPw())
                    .add("after_pw", userVO.getType())
                    .add("phone", userVO.getPhone())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }
        else if(url.equals("DeleteUser.do")){
            RequestBody body = new FormBody.Builder()
                    .add("token",userVO.getToken())
                    .add("pw", userVO.getPw())
                    .build();

            Request request = new Request.Builder()
                    .url("http://jypjun1234.cafe24.com/mobile/"+url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(callback);
        }
    }

}
