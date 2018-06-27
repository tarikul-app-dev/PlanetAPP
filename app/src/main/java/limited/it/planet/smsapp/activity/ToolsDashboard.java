package limited.it.planet.smsapp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.adapter.ButtonAdapter;
import limited.it.planet.smsapp.adapter.ToolsButtonAdapter;
import limited.it.planet.smsapp.util.FontCustomization;
import limited.it.planet.smsapp.util.SessionClear;

public class ToolsDashboard extends AppCompatActivity implements ToolsButtonAdapter.GridViewButtonInterface{
    //Button btnLengthChecker,btnBijoyToUni,btnContact;
    Toolbar toolbar;
    ImageView imgvHome;
    TextView txvHeadTools;
    GridView gvTools;
    public String[] filesnames = {
            "Length Check",
            "Bijoy -> Unicode",
            "Contact Us",
            "Export Contacts"

    };
    public Drawable[] drawables = new Drawable[4];
    public int colors[] = new int[4];
    FontCustomization fontCustomization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_dashboard);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_tools_dashboard);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ToolsDashboard.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(ToolsDashboard.this);
            }
        });
        initViews();
    }

    public void initViews(){
        txvHeadTools = (TextView)findViewById(R.id.txv_head_tools);
        gvTools = (GridView)findViewById(R.id.btn_tools_gridview);
        drawables[0] = this.getResources().getDrawable(R.drawable.ic_sms_length);
        drawables[1] = this.getResources().getDrawable(R.drawable.ic_bijoy_to_unicode);
        drawables[2] = this.getResources().getDrawable(R.drawable.ic_contact_us);
        drawables[3] = this.getResources().getDrawable(R.drawable.ic_settings);

        colors[0] = ContextCompat.getColor(ToolsDashboard.this, R.color.color_sms_length);
        colors[1] =ContextCompat.getColor(ToolsDashboard.this, R.color.color_bijoy_unicode);
        colors[2] = ContextCompat.getColor(ToolsDashboard.this, R.color.color_contact_us);
        colors[3] =  ContextCompat.getColor(ToolsDashboard.this, R.color.color_export_contact);
        gvTools.setAdapter(new ToolsButtonAdapter(ToolsDashboard.this,filesnames,this,drawables,colors));

        fontCustomization = new FontCustomization(ToolsDashboard.this);
        txvHeadTools.setTypeface(fontCustomization.getTexgyreHerosBold());

    }
    @Override
    public void getGridButtonPosition(int position) {
        if(position==0){
            Intent intent = new Intent(ToolsDashboard.this,SMSLengthCheckerActivity.class);
            startActivity(intent);
            // ActivityCompat.finishAffinity(MainActivity.this);
        }else if(position==1){
            Intent intent = new Intent(ToolsDashboard.this,BijoyToUniActivity.class);
            startActivity(intent);
            // ActivityCompat.finishAffinity(MainActivity.this);
        }else if(position==2){
            Intent intent = new Intent(ToolsDashboard.this,ContactActivity.class);
            startActivity(intent);
        }else if(position==3){
            Intent intent = new Intent(ToolsDashboard.this,ContactsActivity.class);
            startActivity(intent);
        }
    }
}
