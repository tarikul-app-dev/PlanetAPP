package limited.it.planet.smsapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import java.net.CookieStore;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.util.Constant;
import limited.it.planet.smsapp.util.MyCookieManager;
import limited.it.planet.smsapp.util.SaveValueSharedPreference;
import limited.it.planet.smsapp.util.SessionClear;

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;
import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveToSharedPreferences;

public class LoginActivity extends DemoActivity {
    WebView webView;
    String mainLoginAPI ="";
    private ProgressDialog progressBar;
    public static String TAG = "cookie";
    AlertDialog alertDialog;
    Toolbar toolbar;
    MyCookieManager myCookieManager;
    ArrayList cookieValueList;
    //ImageView imgvHome;
    //TextView txvToolbarHead;
    SessionClear sessionClear;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_login_activity);
        setSupportActionBar(toolbar);
        sessionClear = new SessionClear(LoginActivity.this);

        myCookieManager = new MyCookieManager();
        webView = (WebView) findViewById(R.id.webview);
       // txvToolbarHead = (TextView)findViewById(R.id.txv_toolbar_head);
        alertDialog = new AlertDialog.Builder(this).create();
        mainLoginAPI = Constant.mainDashboardAPI;
//        progressBar = ProgressDialog.show(LoginActivity.this,
//                              getString(R.string.progress_please_wait), getString(R.string.progress_loading));
//        progressBar.setCancelable(true);
        cookieValueList = new ArrayList();

      //  startProgress();

      loadLoginWebView();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadLoginWebView(){
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");

                Log.d("Webview", url);
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
               // progressBar.isShowing();
               startProgress();

            }
            public void onPageFinished(WebView view, String url) {
                try {
                    sync(url);
                   // txvToolbarHead.setText("Dashboard");
                   saveBoleanValueSharedPreferences("islogin",true,LoginActivity.this);

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    ActivityCompat.finishAffinity(LoginActivity.this);

                }catch (Exception e){
                    e.printStackTrace();
                }
//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//
//                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Log.e(TAG, "Error: " + description);
                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>", "", "");
            }
        });
        webView.loadUrl(mainLoginAPI);
    }
    public void sync(String url) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(LoginActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //it is default true, but hey...
        CookieManager.getInstance().setAcceptCookie(true);

        cookieSyncManager.sync();

        String cookie = cookieManager.getCookie(url);


        String[] cookieValuePairs = cookie.split(";");
       // cookieValueList.add(pairs);

        String pair = cookieValuePairs[4];
        String[] userGroupIDPairs = pair.split("=");
        String userGroupId = userGroupIDPairs[1];
        if(userGroupId.contains("%")){
            String groupId = userGroupId.replace("%22","");
            if(!groupId.contains("%")){
                saveToSharedPreferences("group_id",groupId,LoginActivity.this);
            }
        }



        Log.d(TAG, "cookie ------>"+cookie);

    }

    private void startProgress() {

        progressON("Loading....");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 1000);

    }


}
