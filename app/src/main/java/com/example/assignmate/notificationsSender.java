package com.example.assignmate;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class notificationsSender {
    String userToken,title,body;
    Context context;
    Activity activity;

    private RequestQueue requestQueue;
    private final String postUrl="https://fcm.googleapis.com/fcm/send";
    private final String serverKey="AAAAma5ubbo:APA91bEJFqrrKG_50NuAnbR__e-bEA1tkNKV5ZRK1O5PlOLsR28jMGpoh6C7pGlM3_NcF2BBl0neOL-Oye9pfkvFLZJ8b0Qt6jTF_WX_w488OCkUy0X_76dxs1nmO_FZJ9Vlk6P2O-5z";
    public notificationsSender(String userToken, String title, String body, Context context, Activity activity) {
        this.userToken = userToken;
        this.title = title;
        this.body = body;
        this.context = context;
        this.activity = activity;
    }
    public  void sendNotification(){
        requestQueue = Volley.newRequestQueue(activity);
        JSONObject mainObj = new JSONObject();
        try
        {
            mainObj.put("to",userToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title",title);
            notiObject.put("body",body);
            notiObject.put("icon",R.drawable.google_play_books);




            mainObj.put("notification",notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(context, "inError Response method", Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key="+serverKey);
                    return header;
                }
            };

            requestQueue.add(request);
        }
        catch (JSONException e)
        {
            Log.e("Json exception", "sendNotification: ",e );
        }

    }
}
