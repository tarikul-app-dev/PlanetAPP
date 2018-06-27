package limited.it.planet.smsapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.GridView;
import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.adapter.ButtonAdapter;
import limited.it.planet.smsapp.util.Constant;
import limited.it.planet.smsapp.util.FontCustomization;

public class MainActivity extends AppCompatActivity implements ButtonAdapter.GridViewButtonInterface{
    WebView webView;
    String mainLoginAPI ="";
    String campaignAPI = "";
    String myProfileAPI = "";
    String myAccountAPI = "";
    String logoutAPI = "";

    private ProgressDialog progressBar;
    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    AlertDialog alertDialog;
    Toolbar toolbar;
    private BottomSheetDialog mBottomSheetDialog;
    EditText edtUnicodeCheck;
    TextView txvHeadMain;
   // Button btnLogin,btnCampaigns,btnMyAccount,btnMyProfile,btnSmsLengthChecker;
    GridView btnGridView;
    private Dialog loadingDialog;
    FontCustomization fontCustomization;

    public String[] filesnames = {
            "Dashboard",
            "SMS",
            "Voice",
            "E-Mail",
            "People",
            "Report",
            "Account",
            "Profile",
            "Buy Credit",
            "Tools"

    };
    public Drawable[] drawables = new Drawable[10];
    public int colors[] = new int[10];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar_planet_app);
        setSupportActionBar(toolbar);
        initializeUI();



    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {




        return super.onOptionsItemSelected(item);
    }

    public void initializeUI(){

        webView = (WebView) findViewById(R.id.webview);
        mainLoginAPI = Constant.mainDashboardAPI;
        campaignAPI = Constant.campaignsAPI;
        myProfileAPI = Constant.myProfileAPI;
        myAccountAPI =Constant.myAccountAPI;
        logoutAPI = Constant.logoutAPI;
        alertDialog = new AlertDialog.Builder(this).create();
        btnGridView = (GridView)findViewById(R.id.btn_gridview);
        txvHeadMain = (TextView)findViewById(R.id.txv_main);
        mBottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        fontCustomization = new FontCustomization(MainActivity.this);
        txvHeadMain.setTypeface(fontCustomization.getTexgyreHerosBold());


        drawables[0] = this.getResources().getDrawable(R.drawable.ic_dashboard);
        drawables[1] = this.getResources().getDrawable(R.drawable.ic_sms);
        drawables[2] = this.getResources().getDrawable(R.drawable.ic_voice);
        drawables[3] = this.getResources().getDrawable(R.drawable.ic_mail);
        drawables[4] = this.getResources().getDrawable(R.drawable.ic_people);
        drawables[5] = this.getResources().getDrawable(R.drawable.ic_report);
        drawables[6] = this.getResources().getDrawable(R.drawable.ic_account);
        drawables[7] = this.getResources().getDrawable(R.drawable.ic_profile);
        drawables[8] = this.getResources().getDrawable(R.drawable.ic_top_up);
        drawables[9] = this.getResources().getDrawable(R.drawable.ic_settings);


        colors[0] = ContextCompat.getColor(MainActivity.this, R.color.color_dashboard);
        colors[1] =ContextCompat.getColor(MainActivity.this, R.color.color_sms);
        colors[2] = ContextCompat.getColor(MainActivity.this, R.color.color_voice);
        colors[3] =  ContextCompat.getColor(MainActivity.this, R.color.color_email);
        colors[4] =  ContextCompat.getColor(MainActivity.this, R.color.color_people);
        colors[5] =  ContextCompat.getColor(MainActivity.this, R.color.color_report);
        colors[6] =  ContextCompat.getColor(MainActivity.this, R.color.color_accounts);
        colors[7] =  ContextCompat.getColor(MainActivity.this, R.color.color_profile);
        colors[8] =  ContextCompat.getColor(MainActivity.this, R.color.color_top_up);
        colors[9] =  ContextCompat.getColor(MainActivity.this, R.color.color_tools);


        btnGridView.setAdapter(new ButtonAdapter(MainActivity.this,filesnames,this,drawables,colors));

        fontCustomization = new FontCustomization(MainActivity.this);


    }

    public void sync(String url) {
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(MainActivity.this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //it is default true, but hey...
        CookieManager.getInstance().setAcceptCookie(true);

        cookieSyncManager.sync();

        String cookie = cookieManager.getCookie(url);

        Log.d(TAG, "cookie ------>"+cookie);
    }



    @Override
    public void getGridButtonPosition(int position) {
         if(position==0){
             Intent intent = new Intent(MainActivity.this,DashboardActivity.class);
             startActivity(intent);
            // ActivityCompat.finishAffinity(MainActivity.this);
         }else if(position==1){
             Intent intent = new Intent(MainActivity.this,SMSDashboard.class);
             startActivity(intent);
            // ActivityCompat.finishAffinity(MainActivity.this);
         }else if(position==2){
                Intent intent = new Intent(MainActivity.this,VoiceDashboard.class);
                startActivity(intent);
         }else if(position==3){
//             Intent intent = new Intent(MainActivity.this,EMailDashboardActivity.class);
//             startActivity(intent);
//             ActivityCompat.finishAffinity(MainActivity.this);

             Intent intent = new Intent(MainActivity.this,EmailDashboard.class);
             startActivity(intent);
           //  ActivityCompat.finishAffinity(MainActivity.this);
         } else if(position==4){
             Intent intent = new Intent(MainActivity.this,PeopleActivity.class);
             startActivity(intent);
            // ActivityCompat.finishAffinity(MainActivity.this);
         }else if(position==5){
             Intent intent = new Intent(MainActivity.this,ReportDashboard.class);
             startActivity(intent);
            // ActivityCompat.finishAffinity(MainActivity.this);
         }else if(position==6){
             Intent intent = new Intent(MainActivity.this,AccountDashboard.class);
             startActivity(intent);
           //  ActivityCompat.finishAffinity(MainActivity.this);
         }else if(position==7){
             Intent intent = new Intent(MainActivity.this,MyProfileActivity.class);
             startActivity(intent);
             //ActivityCompat.finishAffinity(MainActivity.this);
         }else if(position==8){
             Intent intent = new Intent(MainActivity.this,BuyCreditActivity.class);
             startActivity(intent);
             //ActivityCompat.finishAffinity(MainActivity.this);
         }else if(position==9){
             Intent intent = new Intent(MainActivity.this,ToolsDashboard.class);
             startActivity(intent);
           //  ActivityCompat.finishAffinity(MainActivity.this);
         }
    }
}
