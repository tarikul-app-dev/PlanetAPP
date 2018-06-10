package limited.it.planet.smsapp.activity;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.util.FontCustomization;
import limited.it.planet.smsapp.util.SessionClear;

public class SMSDashboard extends AppCompatActivity {
    Button btnSMSDashboard,btnPreCamp,btnNewCamp;
    Toolbar toolbar;
    ImageView imgvHome;
    TextView txvHeadSMS;
    SessionClear sessionClear;
    FontCustomization fontCustomization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsdashboard);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_email_dashboard);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SMSDashboard.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(SMSDashboard.this);
            }
        });

        initViews();
    }

    public void initViews(){

        sessionClear = new SessionClear(SMSDashboard.this);
        btnSMSDashboard = (Button)findViewById(R.id.btn_sms_dashboard);
        btnPreCamp = (Button)findViewById(R.id.btn_pre_campaigns);
        btnNewCamp = (Button)findViewById(R.id.btn_new_campaign);
        txvHeadSMS = (TextView)findViewById(R.id.txv_head_sms);

        fontCustomization = new FontCustomization(SMSDashboard.this);
        txvHeadSMS.setTypeface(fontCustomization.getTexgyreHerosBold());
        btnNewCamp.setTypeface(fontCustomization.getTexgyreHerosBold());
        btnPreCamp.setTypeface(fontCustomization.getTexgyreHerosBold());
        btnSMSDashboard.setTypeface(fontCustomization.getTexgyreHerosBold());

        btnSMSDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SMSDashboard.this,SMSDashboardActivity.class);
                startActivity(intent);
              //  ActivityCompat.finishAffinity(SMSDashboard.this);

            }
        });

        btnPreCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SMSDashboard.this,AllCampaignActivity.class);
                startActivity(intent);
               // ActivityCompat.finishAffinity(SMSDashboard.this);
            }
        });

        btnNewCamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SMSDashboard.this,NewCampActivity.class);
                startActivity(intent);
               // ActivityCompat.finishAffinity(SMSDashboard.this);
            }
        });
    }
//    @Override
//    public void onPause() {
//        super.onPause();
//        sessionClear.clearSessionWhenPause();
//    }
}
