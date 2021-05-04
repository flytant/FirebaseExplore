package com.flytant.firebaseexplore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstagramActivity extends AppCompatActivity {

    private TextView tvInstaUsername;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private int j = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);

        tvInstaUsername = findViewById(R.id.textView3);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public void loginInsta(View view) {
//        Intent intent = new Intent(this, InstaLoginActivity.class);
//        startActivityForResult(intent, 1001);
        j = j + 1;
        registerWithEmail("test" + j + "@gmail.com");
    }

    private void registerWithEmail(String email) {
        final Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("password", "123456789");
        map.put("image", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRCL6MMX9vx_QOHuCzODuFJQSsfX5bYKqm4Zg&usqp=CAU");
        map.put("name", "Sandeep Malik");
        mAuth.createUserWithEmailAndPassword(email, "123456789")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {
                            FirebaseUser user = task.getResult().getUser();
                            map.put("userId", user.getUid());
                            firestore.collection("users")
                                    .document(user.getUid())
                                    .set(map);
                            Toast.makeText(InstagramActivity.this, "User register", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(InstagramActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//    public boolean isAppInstalled(String packageName) {
//        try {
////            PackageInfo pkgInfo = getPackageManager().getApplicationInfo(packageName, 0).enabled;
////            return pkgInfo.toString().equals(packageName);
//            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1001) {
//            code = data.getStringExtra("code");
//            getInstagramToken();
//        }
//    }
//
//    private void getInstagramToken() {
//        String url = "https://api.instagram.com/oauth/access_token";
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            token = jsonObject.getString("access_token");
//                            instaId = jsonObject.getString("user_id");
//                            getInstaDetails();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(InstagramActivity.this, "Api Error", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Nullable
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> MyData = new HashMap<String, String>();
//                MyData.put("client_id", "191312392530647");
//                MyData.put("client_secret", "6daaea5f1568cd3e8ff218e7e976938c");
//                MyData.put("redirect_uri", "https://flytant.com/");
//                MyData.put("grant_type", "authorization_code");
//                MyData.put("code", code);
//                return MyData;
//            }
//        };
//        queue.add(request);
//    }
//
//    private void getInstaDetails() {
//        String url = "https://graph.facebook.com/v3.2/" + instaId + "?fields=biography, " +
//                "id, ig_id, followers_count, follows_count, media_count, name, " +
//                "profile_picture_url, username&access_token=" + token;
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            Log.d("TAG", "onResponse: " + response);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(InstagramActivity.this, "Api error: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        queue.add(request);
//    }
}