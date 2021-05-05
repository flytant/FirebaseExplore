package com.flytant.firebaseexplore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.TextMessage;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private RecyclerView rvUsers;
    private ArrayList<User> userList;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private UserAdapter userAdapter;
    private DocumentReference docRef;
    private TextView tvResult;
    private Translator englishHindiTranslator;
    private TranslatorOptions options;
    private DownloadConditions conditions;
    private SmartReplyGenerator smartReply;
    private ArrayList<TextMessage> conversation;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String fbId, fbPageId, instaPageId, fbToken;
    private AccessToken accessToken;
    private String instaLoginUrl = "https://www.instagram.com/oauth/authorize?client_id=191312392530647&amp;redirect_uri=https://flytant.com/&amp;scope=user_profile,user_media&amp;response_type=code";

    private OnUserItemClickListener listener = new OnUserItemClickListener() {
        @Override
        public void onSaveButtonClick(String uId) {
            docRef.update("userList", FieldValue.arrayUnion(uId));
        }

        @Override
        public void onDeleteButtonClick(String uId) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userList = new ArrayList<>();

        edtEmail = findViewById(R.id.editText);
        edtPassword = findViewById(R.id.editText1);
        tvResult = findViewById(R.id.textView2);
        rvUsers = findViewById(R.id.recyclerView);
        loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions("email", "public_profile", "instagram_basic", "pages_show_list");
        userAdapter = new UserAdapter(this, userList, listener);
        rvUsers.setAdapter(userAdapter);
        conversation = new ArrayList<>();

        Log.d("TAG", "All Language: "+TranslateLanguage.getAllLanguages());

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
//        docRef = firestore.collection("list")
//                .document("M89JQQsi4viUsTm8b5Rb");
//        options =
//                new TranslatorOptions.Builder()
//                        .setSourceLanguage(TranslateLanguage.ENGLISH)
//                        .setTargetLanguage(TranslateLanguage.HINDI)
//                        .build();
//        englishHindiTranslator =
//                Translation.getClient(options);
//        conditions = new DownloadConditions.Builder()
//                .requireWifi()
//                .build();
//        smartReply = SmartReply.getClient();
//        LoginManager.getInstance().logInWithReadPermissions(this,
//                Arrays.asList("email", "pages_show_list", "pages_read_engagement",
//                        "pages_read_user_content", "pages_manage_posts"));
//        callbackManager = CallbackManager.Factory.create();
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                fbId = loginResult.getAccessToken().getUserId();
//                fbToken = loginResult.getAccessToken().getToken();
//                accessToken = loginResult.getAccessToken();
//                handleFacebookToken(accessToken);
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//
//        edtEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                translateText();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        getAllUsers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registerUser(View view) {
//        tvResult.setText("");
//        translateText();
//        getFbAccountForInsta();
        getfbUserDetails();

//        conversation.add(TextMessage.createForLocalUser(edtEmail.getText().toString(),
//                System.currentTimeMillis()));
//        smartReply.suggestReplies(conversation)
//                .addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
//                    @Override
//                    public void onSuccess(SmartReplySuggestionResult smartReplySuggestionResult) {
//                        if (smartReplySuggestionResult.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
//                            tvResult.setText("No Suggestions");
//                        } else if (smartReplySuggestionResult.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
//                            for (SmartReplySuggestion suggestion : smartReplySuggestionResult.getSuggestions()) {
//                                if (tvResult.getText().toString().isEmpty()) {
//                                    tvResult.setText(suggestion.getText());
//                                } else {
//                                    tvResult.append("\n" + suggestion.getText());
//                                }
//                            }
//                        } else {
//                            tvResult.setText("No Suggestions");
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // Task failed with an exception
//                        // ...
//                    }
//                });
    }

    private void getfbUserDetails() {
        String url = "https://graph.facebook.com/v10.0/" + fbId + "?fields=email,about,picture{url}" +
                ",name&access_token=" + fbToken;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(MainActivity.this, jsonObject.getString("name"), Toast.LENGTH_SHORT).show();
                            getFbAccountForInsta();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "VolleyError", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void getfbPost() {
        String url = "https://graph.facebook.com/" + fbPageId + "/feed?access_token=" + fbToken;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getJSONArray("data").length() > 0) {
                                Toast.makeText(MainActivity.this, "Post mil gyi", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Koi bhi post ni mili", Toast.LENGTH_SHORT).show();
                            }
                            Log.d("TAG", "onResponse: " + jsonObject);
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, "Kuch exception h", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Kuch error h", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    private void getFbAccountForInsta() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://graph.facebook.com/v10.0/me/accounts?access_token=" + fbToken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            fbPageId = jsonObject.getJSONArray("data")
                                    .getJSONObject(0)
                                    .getString("id");
                            getfbPost();
//                            getInstaId();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(stringRequest);
    }

    private void getInstaId() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://graph.facebook.com/v10.0/" + fbPageId + "?fields=instagram_business_account&access_token=" + fbToken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("TAG", "fbResponse: " + jsonObject);
                            if (response.contains("instagram_business_account")) {
                                instaPageId = jsonObject.getJSONObject("instagram_business_account")
                                        .getString("id");
                            } else {
                                instaPageId = jsonObject.getString("id");
                            }
                            getInstaData();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(stringRequest);
    }

    private void getInstaData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://graph.facebook.com/v10.0/" + instaPageId + "?" +
                        "fields=followers_count,media_count,ig_id,biography,name,profile_picture_url,media{caption,comments_count,like_count,media_url}" +
                        "&access_token=" + fbToken,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("TAG", "fbResponse: " + jsonObject);
                            Toast.makeText(MainActivity.this, "Data nikl gya", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvResult.setText(error.getMessage());
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(stringRequest);
    }

    private void translateText() {
        englishHindiTranslator.downloadModelIfNeeded(conditions)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            englishHindiTranslator.translate(edtEmail.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            if (s != null) {
                                                tvResult.setText(s);
                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllUsers() {
        firestore.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        try {
                            for (DocumentSnapshot snapshot : task.getResult()) {
                                User user = snapshot.toObject(User.class);
                                userList.add(user);
                            }
                            userAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public interface OnUserItemClickListener {
        void onSaveButtonClick(String uId);

        void onDeleteButtonClick(String uId);
    }

    private void handleFacebookToken(AccessToken accessToken) {
        final AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            linkUser(credential);
                        }
                    }
                });
    }

    private void linkUser(final AuthCredential credential) {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "No User Found", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser user = mAuth.getCurrentUser();
            String id = user.getProviderData().get(0).getProviderId();
            for (int i = 0; i < user.getProviderData().size(); i++) {
                Log.d("TAG", "linkUser: " + user.getProviderData().get(i).getProviderId());
                id = user.getProviderData().get(i).getProviderId();
            }
            mAuth.getCurrentUser().unlink(id)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "User unlinked", Toast.LENGTH_SHORT).show();
                                linkNewUser(credential);
                            } else {
                                Toast.makeText(MainActivity.this,
                                        task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void linkNewUser(AuthCredential credential) {
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this,
                                    task.getResult().getUser().getUid(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void unlinkUser() {

    }
}