package com.flytant.firebaseexplore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InstaLoginActivity extends AppCompatActivity {

    private WebView webView;
    private String url = "https://www.instagram.com/oauth/authorize?client_id=191312392530647&amp;redirect_uri=https://flytant.com/&amp;scope=user_profile,user_media&amp;response_type=code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_login);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebClient());
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        if (!url.contains("https://")) {
            url = "https://" + url;
        }
        webView.loadUrl(url);
    }

    private class WebClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("https://flytant.com/?code=")) {
                Intent intent = new Intent();
                intent.putExtra("code", url.replace("https://flytant.com/?code=", "")
                        .replace("#_", ""));
                setResult(1001, intent);
                finish();
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        public void onPageFinished(WebView view, String url) {
        }

    }
}