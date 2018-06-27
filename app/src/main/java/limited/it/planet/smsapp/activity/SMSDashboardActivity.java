package limited.it.planet.smsapp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.util.Constant;
import limited.it.planet.smsapp.util.FontCustomization;
import limited.it.planet.smsapp.util.SessionClear;
import me.drakeet.materialdialog.MaterialDialog;

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class SMSDashboardActivity extends DemoActivity {
    Toolbar toolbar;
    private ProgressDialog progressBar;
    AlertDialog alertDialog;
    WebView webView;
    //ImageView imgvHome;
    MaterialDialog alert;;
    String preCampaignAPI = "";
    String newCampaignAPI = "";
    String dashboardAPI = "";

    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    TextView txvHeadlineToolbar;
    FontCustomization fontCustomization;

    SessionClear sessionClear;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_sms);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_campaigns_activity);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // onBackPressed();

                Intent intent = new Intent(SMSDashboardActivity.this,SMSDashboard.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(SMSDashboardActivity.this);
            }
        });

        initViews();
        if (savedInstanceState == null) {
            webView.loadUrl(dashboardAPI);
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
    public void initViews(){
        sessionClear = new SessionClear(SMSDashboardActivity.this);
        webView = (WebView) findViewById(R.id.webview);
        txvHeadlineToolbar = (TextView)findViewById(R.id.txv_headline_sms);
        fontCustomization = new FontCustomization(SMSDashboardActivity.this);

        alertDialog = new AlertDialog.Builder(this).create();
        preCampaignAPI = Constant.campaignsAPI;
        dashboardAPI = Constant.smsDashboardAPI;
        newCampaignAPI = Constant.newCampaignsAPI;

        // listDialogue();
        txvHeadlineToolbar.setTypeface(fontCustomization.getTexgyreHerosBold());


//        progressBar = ProgressDialog.show(SMSDashboardActivity.this,
//                getString(R.string.progress_please_wait), getString(R.string.progress_loading));

        startProgress();
        loadDashboardWebView();
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
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
      //  webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //progressBar.isShowing();
                Log.d(TAG_PRO, "progressbar ------>"+"start");
            }
            public void onPageFinished(WebView view, String url) {
                txvHeadlineToolbar.setText("Dashboard");

//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//
//                    Log.d(TAG_PRO, "progressbar ------>"+"finish");
//
//                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Log.e(TAG, "Error: " + description);
                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>", "", "");
            }
        });
        webView.loadUrl(dashboardAPI);
    }



    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sms_dashboard, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.sms_dashboard_logout:

                clearCookies(SMSDashboardActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,SMSDashboardActivity.this);

                Intent intent = new Intent(SMSDashboardActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(SMSDashboardActivity.this);
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
//    public void onPause() {
//        super.onPause();
//        sessionClear.clearSessionWhenPause();
//    }
}
