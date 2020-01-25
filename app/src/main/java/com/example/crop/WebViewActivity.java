package com.example.crop;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        WebView webView = findViewById(R.id.webview);
        final ProgressBar webviewLoadingProgressBar = findViewById(R.id.webviewLoadingProgressBar);
        webviewLoadingProgressBar.setVisibility(View.VISIBLE);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(getResources().getString(R.string.url));
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                webviewLoadingProgressBar.setVisibility(View.GONE);
            }
        });


    }
}
