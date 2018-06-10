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

public class EmailDashboard extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgvHome;
    Button btnEmailDashboard,btnNewECamp;
    TextView txvHeadEmail;
    SessionClear sessionClear;
    FontCustomization fontCustomization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_dashboard);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_email_dashboard);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailDashboard.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(EmailDashboard.this);
            }
        });

        initViews();

    }


    public void initViews(){

        sessionClear = new SessionClear(EmailDashboard.this);
        btnEmailDashboard = (Button)findViewById(R.id.btn_email_dashboard);
        btnNewECamp = (Button)findViewById(R.id.btn_email_campaigns_new);
        txvHeadEmail = (TextView)findViewById(R.id.txv_head_email);
        fontCustomization = new FontCustomization(EmailDashboard.this);
        btnEmailDashboard.setTypeface(fontCustomization.getTexgyreHerosBold());
        btnNewECamp.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvHeadEmail.setTypeface(fontCustomization.getTexgyreHerosBold());


        btnEmailDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailDashboard = new Intent(EmailDashboard.this,EMailDashboardActivity.class);
                startActivity(emailDashboard);
              //  ActivityCompat.finishAffinity(EmailDashboard.this);
            }
        });

        btnNewECamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailDashboard = new Intent(EmailDashboard.this,NewEmailCampActivity.class);
                startActivity(emailDashboard);
                //ActivityCompat.finishAffinity(EmailDashboard.this);
            }
        });

    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        sessionClear.clearSessionWhenPause();
//    }
}
