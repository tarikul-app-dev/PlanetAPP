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
import limited.it.planet.smsapp.util.ViewDialog;
import me.drakeet.materialdialog.MaterialDialog;

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class VoiceAllCampActivity extends DemoActivity {
    Toolbar toolbar;
    WebView webView;
    String voiceCampaign = "";
    private ProgressDialog progressBar;
    public static String TAG = "cookie";
    AlertDialog alertDialog;

    MaterialDialog alert;
    ViewDialog viewDialog;
    TextView txvToolbarHead;
    FontCustomization fontCustomization;
    SessionClear sessionClear;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_campaign);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_voice_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                Intent intent = new Intent(VoiceAllCampActivity.this,VoiceDashboard.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(VoiceAllCampActivity.this);
            }
        });


        initializeUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void initializeUI(){
        sessionClear = new SessionClear(VoiceAllCampActivity.this);
        voiceCampaign = Constant.voiceCampaignAPI;

        webView = (WebView) findViewById(R.id.webview);
        alertDialog = new AlertDialog.Builder(this).create();

        txvToolbarHead =(TextView)findViewById(R.id.txv_toolbar_head);

        viewDialog = new ViewDialog();


//        progressBar = ProgressDialog.show(VoiceAllCampActivity.this,
//                getString(R.string.progress_please_wait), getString(R.string.progress_loading));
//        progressBar.setCancelable(true);

        loadVoiceCampaignWebView();

        fontCustomization = new FontCustomization(VoiceAllCampActivity.this);
        txvToolbarHead.setTypeface(fontCustomization.getTexgyreHerosBold());

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadVoiceCampaignWebView(){
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
                Log.d("My Webview", url);
                // linearLayoutWarning.setVisibility(View.VISIBLE);
                viewDialog.showDialog(VoiceAllCampActivity.this,"This Service is not Available for this account.");
                viewDialog.canCelDialog();
                //   view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {

//                if (progressBar.isShowing()) {
//                    progressBar.dismiss();
//
//                }

            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
              //  progressBar.isShowing();
                startProgress();

            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Log.e(TAG, "Error: " + description);
                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>", "", "");
            }
        });


        webView.loadUrl(voiceCampaign);
    }

    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_voice_activity, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.voice_logout:

                clearCookies(VoiceAllCampActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,VoiceAllCampActivity.this);

                Intent intent = new Intent(VoiceAllCampActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(VoiceAllCampActivity.this);
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
