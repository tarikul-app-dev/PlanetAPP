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

public class PeopleActivity extends DemoActivity {
    Toolbar toolbar;
    WebView webView;
    String peopleAPICall = "";

    private ProgressDialog progressBar;
    public static String TAG = "cookie";
    AlertDialog alertDialog;
    ImageView imgvHome;
    MaterialDialog alert;
    ViewDialog viewDialog;
    TextView txvToolbarHead;
    FontCustomization fontCustomization;
    SessionClear sessionClear;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_people_activity);
        setSupportActionBar(toolbar);
        initializeUI();

        if (savedInstanceState == null) {
            webView.loadUrl(peopleAPICall);
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
    public void initializeUI(){

        sessionClear = new SessionClear(PeopleActivity.this);
        peopleAPICall = Constant.peopleAPI;

        webView = (WebView) findViewById(R.id.webview);
        alertDialog = new AlertDialog.Builder(this).create();
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        txvToolbarHead =(TextView)findViewById(R.id.txv_headline_people);

        viewDialog = new ViewDialog();

        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeopleActivity.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(PeopleActivity.this);
            }
        });
//        progressBar = ProgressDialog.show(AllReportActivity.this,
//                getString(R.string.progress_please_wait), getString(R.string.progress_loading));
        // loadReportWebView();

        fontCustomization = new FontCustomization(PeopleActivity.this);
        txvToolbarHead.setTypeface(fontCustomization.getTexgyreHerosBold());

//        progressBar = ProgressDialog.show(PeopleActivity.this,
//                getString(R.string.progress_please_wait), getString(R.string.progress_loading));
//        progressBar.setCancelable(true);

        loadPeopleWebView();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void loadPeopleWebView(){
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
                Log.d("My Webview", url);
                // linearLayoutWarning.setVisibility(View.VISIBLE);
                // viewDialog.showDialog(AllReportActivity.this,"This Service is not Available for this account.");
                view.loadUrl(url);
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
               // progressBar.isShowing();
                startProgress();
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Log.e(TAG, "Error: " + description);
                view.loadData("<html>!Your Device is Offline.Please Connect Internet.</html>", "", "");
            }
        });


        webView.loadUrl(peopleAPICall);
    }


    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_people_activity, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.people_logout:

                clearCookies(PeopleActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,PeopleActivity.this);

                Intent intent = new Intent(PeopleActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(PeopleActivity.this);
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
