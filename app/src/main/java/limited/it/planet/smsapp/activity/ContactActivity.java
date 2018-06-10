package limited.it.planet.smsapp.activity;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.TextView;
import limited.it.planet.smsapp.R;
import android.Manifest;

import limited.it.planet.smsapp.util.FontCustomization;
import me.drakeet.materialdialog.MaterialDialog;

import static limited.it.planet.smsapp.util.SaveValueSharedPreference.saveBoleanValueSharedPreferences;

public class ContactActivity extends DemoActivity {
    Toolbar toolbar;
    public static String TAG = "cookie";
    public static String TAG_PRO = "progress";
    ImageView imgvHome,imgvMap;
    TextView txvMap,txvCallHotline,txvCallSkype,txvPlanetIT,txvContactUs,txvAddress,txvEmail;
    MaterialDialog alert;
    public static final int REQUEST_PERM_CALL = 102;
    FontCustomization fontCustomization;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                Intent intent = new Intent(ContactActivity.this,ToolsDashboard.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(ContactActivity.this);
            }
        });
        imgvMap = (ImageView)findViewById(R.id.imgv_map);
        txvMap = (TextView)findViewById(R.id.txv_map);
        txvCallHotline = (TextView)findViewById(R.id.txv_hotline_call);
        txvCallSkype = (TextView)findViewById(R.id.txv_skype_call);
        txvPlanetIT = (TextView)findViewById(R.id.txv_planet_it);
        txvContactUs = (TextView)findViewById(R.id.txv_contact_us);
        txvAddress = (TextView)findViewById(R.id.txv_address);
        txvEmail = (TextView)findViewById(R.id.txv_email);

        fontCustomization = new FontCustomization(ContactActivity.this);
        txvPlanetIT.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvCallHotline.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvCallSkype.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvContactUs.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvAddress.setTypeface(fontCustomization.getTexgyreHerosRegular());
        txvEmail.setTypeface(fontCustomization.getTexgyreHerosRegular());



        imgvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "https://www.google.com/maps/place/Planet+IT/@23.7715952,90.4094301,17z/data=!3m1!4b1!4m5!3m4!1s0x3755c7788a1ea509:0x3317a74c79b39f34!8m2!3d23.7715903!4d90.4116188";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        txvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "https://www.google.com/maps/place/Planet+IT/@23.7715952,90.4094301,17z/data=!3m1!4b1!4m5!3m4!1s0x3755c7788a1ea509:0x3317a74c79b39f34!8m2!3d23.7715903!4d90.4116188";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


        txvCallHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ContactActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERM_CALL);

                } else{
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:+88 01616 752638" ));
                    startActivity(intent);
                }


            }
        });


        txvCallSkype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = getPackageManager();
                Intent skype = packageManager.getLaunchIntentForPackage("com.skype.raider");
               // skype.setData(Uri.parse("tel:+88 01616 752638"));
                startActivity(skype);
            }
        });
        txvPlanetIT.startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_animate));
    }



    //menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Sudip: 20170212
        switch (item.getItemId()) {

            case R.id.contact_logout:

                clearCookies(ContactActivity.this);
                saveBoleanValueSharedPreferences("islogin",false,ContactActivity.this);

                Intent intent = new Intent(ContactActivity.this,LoginActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(ContactActivity.this);
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
    @Override
    public void onResume() {
        super.onResume();
        txvPlanetIT = (TextView)findViewById(R.id.txv_planet_it);
        txvPlanetIT.startAnimation(AnimationUtils.loadAnimation(this, R.anim.text_animate));

    }
}
