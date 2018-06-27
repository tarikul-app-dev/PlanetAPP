package limited.it.planet.smsapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.util.Constant;
import limited.it.planet.smsapp.util.FontCustomization;
import limited.it.planet.smsapp.util.MyCookieManager;
import limited.it.planet.smsapp.util.SessionClear;

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;
import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveToSharedPreferences;

public class DashboardActivity extends DemoActivity {
    WebView webView;
    String mainLoginAPI ="";
    private ProgressDialog progressBar;
    public static String TAG = "cookie";
    AlertDialog alertDialog;
    Toolbar toolbar;
    MyCookieManager myCookieManager;
    ArrayList cookieValueList;
    ImageView imgvHome;
    SessionClear sessionClear;
    TextView txvHeadDashboard;
    FontCustomization fontCustomization;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_dashboard_activity);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(DashboardActivity.this);
            }
        });

        fontCustomization = new FontCustomization(DashboardActivity.this);
        txvHeadDashboard = (TextView)findViewById(R.id.txv_dashboard_head);
        txvHeadDashboard.setTypeface(fontCustomization.getTexgyreHerosBold());

        sessionClear = new SessionClear(DashboardActivity.this);
        myCookieManager = new MyCookieManager();
        webView = (WebView) findViewById(R.id.webview);
        alertDialog = new AlertDialog.Builder(this).create();
        mainLoginAPI = Constant.mainDashboardAPI;
//        progressBar = ProgressDialog.show(DashboardActivity.this,
//                getString(R.string.progress_please_wait), getString(R.string.progress_loading));
//        progressBar.setCancelable(true);

        cookieValueList = new ArrayList();
        loadDashboardWebView();
        if (savedInstanceState == null) {
            webView.loadUrl(mainLoginAPI);
        }

    }
    //To Prevent Webview load when rotate
    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadDashboardWebView(){
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.clearCache(true);
        webView.clearHistory();

        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true); // enable javascript

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
       // webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(true);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");

                Log.d("My Webview", url);
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
              //  progressBar.isShowing();
                startProgress();

            }
            public void onPageFinished(WebView view, String url) {
                try {
                    sync(url);

                }catch (Exception e){
                    e.printStackTrace();
                }
//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//
//                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>",  "text/html", "UTF-8");



            }
        });
        webView.loadUrl(mainLoginAPI);
    }
    public void sync(String url) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(DashboardActivity.this);
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
                saveToSharedPreferences("group_id",groupId,DashboardActivity.this);
            }
        }



        Log.d(TAG, "cookie ------>"+cookie);

    }

    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.menu_dashboard_logout:
//                CookieManager cookieManager = CookieManager.getInstance();
//                cookieManager.setAcceptCookie(false);
                clearCookies(DashboardActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,DashboardActivity.this);

                 Intent intent = new Intent(DashboardActivity.this,LoginActivity.class);
                 startActivity(intent);
                 ActivityCompat.finishAffinity(DashboardActivity.this);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }


        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("deprecation")
    public static void clearCookies(Context context)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
           // Log.d(C.TAG, "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            //Log.d(C.TAG, "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }

    private void startProgress() {

        progressON("Loading....");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        }, 5000);

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        sessionClear.clearSessionWhenPause();
//    }
}
