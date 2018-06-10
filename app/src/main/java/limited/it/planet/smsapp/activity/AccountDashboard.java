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

public class AccountDashboard extends AppCompatActivity {
    Button btnAllAccounts,btnMyAccount;
    Toolbar toolbar;
    ImageView imgvHome;
    TextView txvHeadAcc;
    SessionClear sessionClear;
    FontCustomization fontCustomization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_dashboard);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_account_dashboard);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDashboard.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(AccountDashboard.this);
            }
        });

        initViews();
    }

    public void initViews(){
        btnAllAccounts = (Button)findViewById(R.id.btn_view_all_accounts);
        btnMyAccount = (Button)findViewById(R.id.btn_view_my_account);
        txvHeadAcc = (TextView)findViewById(R.id.txv_head_account);
        sessionClear = new SessionClear(AccountDashboard.this);
        fontCustomization = new FontCustomization(AccountDashboard.this);
        btnAllAccounts.setTypeface(fontCustomization.getTexgyreHerosBold());
        btnMyAccount.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvHeadAcc.setTypeface(fontCustomization.getTexgyreHerosBold());

        btnAllAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDashboard.this,AllAccountsActivity.class);
                startActivity(intent);
                //ActivityCompat.finishAffinity(AccountDashboard.this);
            }
        });

        btnMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountDashboard.this,MyAccountActivity.class);
                startActivity(intent);
               // ActivityCompat.finishAffinity(AccountDashboard.this);
            }
        });

    }


//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        sessionClear.clearSessionWhenPause();
//    }
}
