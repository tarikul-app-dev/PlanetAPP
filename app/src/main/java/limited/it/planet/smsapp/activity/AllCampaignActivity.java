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

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.util.Constant;
import limited.it.planet.smsapp.util.FontCustomization;
import limited.it.planet.smsapp.util.SessionClear;
import me.drakeet.materialdialog.MaterialDialog;

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class AllCampaignActivity extends DemoActivity {
    Toolbar toolbar;
    private ProgressDialog progressBar;
    AlertDialog alertDialog;
    WebView webView;
    ImageView imgvHome;
    MaterialDialog alert;;
    String preCampaignAPI = "";

    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    TextView txvHeadlineToolbar;
    FontCustomization fontCustomization;

    SessionClear sessionClear;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_campaign);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);

        toolbar = (Toolbar)findViewById(R.id.toolbar_pre_camp_activity);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();

                Intent intent = new Intent(AllCampaignActivity.this,SMSDashboard.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(AllCampaignActivity.this);
            }
        });
        initViews();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initViews(){
        sessionClear = new SessionClear(AllCampaignActivity.this);
        webView = (WebView) findViewById(R.id.webview);
        txvHeadlineToolbar = (TextView)findViewById(R.id.txv_toolbar_head);
        fontCustomization = new FontCustomization(AllCampaignActivity.this);

        alertDialog = new AlertDialog.Builder(this).create();
        preCampaignAPI = Constant.campaignsAPI;

        // listDialogue();
        txvHeadlineToolbar.setTypeface(fontCustomization.getTexgyreHerosBold());


//        progressBar = ProgressDialog.show(AllCampaignActivity.this,
//                getString(R.string.progress_please_wait), getString(R.string.progress_loading));
//        progressBar.setCancelable(true);


        loadPreviousCampaignsWebView();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadPreviousCampaignsWebView(){
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
        // webView.getSettings().setUseWideViewPort(true);

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
               // progressBar.isShowing();
                Log.d(TAG_PRO, "progressbar ------>"+"start");
                startProgress();
            }
            public void onPageFinished(WebView view, String url) {

                txvHeadlineToolbar.setText("Campaigns");

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>", "", "");
            }
        });
        webView.loadUrl(preCampaignAPI);
    }


    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_campaign, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.all_campaign_logout:

                clearCookies(AllCampaignActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,AllCampaignActivity.this);

                Intent intent = new Intent(AllCampaignActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(AllCampaignActivity.this);
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
