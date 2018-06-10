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

public class ReportDashboard extends AppCompatActivity {
    Button btnAllReports,btnLogs;
    Toolbar toolbar;
    ImageView imgvHome;
    TextView txvHeadReport;
    SessionClear sessionClear;
    FontCustomization fontCustomization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_dashboard);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);

        toolbar = (Toolbar)findViewById(R.id.toolbar_account_dashboard);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportDashboard.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(ReportDashboard.this);
            }
        });
        initViews();
    }


    public void initViews(){

        sessionClear = new SessionClear(ReportDashboard.this);
        btnAllReports = (Button)findViewById(R.id.btn_all_reports);
        btnLogs = (Button)findViewById(R.id.btn_logs);
        txvHeadReport = (TextView)findViewById(R.id.txv_head_report);
        fontCustomization = new FontCustomization(ReportDashboard.this);
        btnAllReports.setTypeface(fontCustomization.getTexgyreHerosBold());
        btnLogs.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvHeadReport.setTypeface(fontCustomization.getTexgyreHerosBold());

        btnAllReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportDashboard.this,AllReportActivity.class);
                startActivity(intent);
               // ActivityCompat.finishAffinity(ReportDashboard.this);
            }
        });

        btnLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportDashboard.this,LogsReportActivity.class);
                startActivity(intent);
              //  ActivityCompat.finishAffinity(ReportDashboard.this);
            }
        });

    }
}
