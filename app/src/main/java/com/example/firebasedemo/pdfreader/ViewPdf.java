package com.example.firebasedemo.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.firebasedemo.databinding.ActivityViewPdfBinding;

import java.net.URLEncoder;

public class ViewPdf extends AppCompatActivity {
    ActivityViewPdfBinding binding;
    String fileName,fileUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityViewPdfBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.viewPdf.getSettings().setJavaScriptEnabled(true);

        fileName=getIntent().getStringExtra("filename");
        fileUrl=getIntent().getStringExtra("fileurl");

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle(fileName);
        progressDialog.setMessage("File Opening...!");

        binding.viewPdf.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });

        String url="";
        try {
            url= URLEncoder.encode(fileUrl,"UTF-8");
        }
        catch (Exception e){}
        binding.viewPdf.loadUrl("http:docs.google.com/gview?embedded=true&url="+url);
    }
}