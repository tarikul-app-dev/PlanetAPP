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

public class VoiceDashboard extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgvHome;
    Button btnAllVCampaign,btnVNewCampaign;
    TextView txvHeadVoice;
    FontCustomization fontCustomization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_dashboard);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_voice_dashboard);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoiceDashboard.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(VoiceDashboard.this);
            }
        });

        initViews();
    }


    public void initViews(){
        btnAllVCampaign = (Button)findViewById(R.id.btn_all_v_campaign);
        btnVNewCampaign = (Button)findViewById(R.id.btn_new_v_campaigns);
        txvHeadVoice = (TextView)findViewById(R.id.txv_head_voice);
        fontCustomization = new FontCustomization(VoiceDashboard.this);

        btnAllVCampaign.setTypeface(fontCustomization.getTexgyreHerosBold());
        btnVNewCampaign.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvHeadVoice.setTypeface(fontCustomization.getTexgyreHerosBold());

        btnAllVCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoiceDashboard.this,VoiceAllCampActivity.class);
                startActivity(intent);
               // ActivityCompat.finishAffinity(VoiceDashboard.this);
            }
        });

        btnVNewCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VoiceDashboard.this,NewVoiceCampActivity.class);
                startActivity(intent);
              //  ActivityCompat.finishAffinity(VoiceDashboard.this);
            }
        });

    }
}
