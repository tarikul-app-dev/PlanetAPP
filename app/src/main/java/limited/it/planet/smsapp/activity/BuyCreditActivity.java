package limited.it.planet.smsapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import limited.it.planet.smsapp.util.FontCustomization;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.util.Constant;

public class BuyCreditActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText edtUserName,edtBTrans;
    Button btnRefresh,btnSubmit;
    String bKashAPI = "";
    private Dialog loadingDialog;
    String responseBodyText = "";
    FontCustomization fontCustomization;
    TextView txvHeadRecharge,txvPayMethod;

    String userName = "";
    String checkUserAPI = "";
    String getUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_credit);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
        toolbar = (Toolbar)findViewById(R.id.toolbar_top_up_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // onBackPressed();
                Intent intent = new Intent(BuyCreditActivity.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(BuyCreditActivity.this);
            }
        });


        initViews();

    }

    public void initViews(){
        edtUserName = (EditText)findViewById(R.id.edt_user_name);
        edtBTrans = (EditText)findViewById(R.id.edt_bkash_trxid);
        btnRefresh = (Button)findViewById(R.id.btn_refresh);
        btnSubmit = (Button)findViewById(R.id.btn_submit);
        txvHeadRecharge = (TextView)findViewById(R.id.txv_toolbar_head);
        txvPayMethod = (TextView)findViewById(R.id.txv_pay_method);
        //bKashAPI = Constant.bkashAPI;
        fontCustomization = new FontCustomization(BuyCreditActivity.this);
        txvHeadRecharge.setTypeface(fontCustomization.getTexgyreHerosBold());
        txvPayMethod.setTypeface(fontCustomization.getTexgyreHerosBold());

        //check text when user changed a text
        edtUserName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                userName = edtUserName.getText().toString();
                checkUserAPI = Constant.checkUserAPI + userName ;
                new CheckUserTask().execute();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtUserName.getText().toString().isEmpty()){
                    Toast.makeText(BuyCreditActivity.this, "You must give user name and Transaction Id", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(getUser.equals("1")){
                    userName = edtUserName.getText().toString();
                    String bKashTrxID = edtBTrans.getText().toString();
                    bKashAPI =  Constant.bkashAPI + "user=" + userName + "&trx=" +  bKashTrxID;
                    new SubmitPaymantAsync().execute();
                }else {
                    Toast.makeText(BuyCreditActivity.this,"Invalid User", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtUserName.setText("");
                edtBTrans.setText("");
            }
        });

        if(!isConnectingToInternet(BuyCreditActivity.this)){
            Toast.makeText(BuyCreditActivity.this,"OOPS,Your Device is Offline,Please Connect Net for Buy Credit", Toast.LENGTH_SHORT).show();
        }

    }

    public void openDialog(String responseText) {
        final Dialog dialog = new Dialog(BuyCreditActivity.this); // Context, this, etc.
        dialog.setContentView(R.layout.custom_dialog_payment);
        TextView txvResponseMsg = (TextView) dialog.findViewById(R.id.dialog_info);
        txvResponseMsg.setText(responseText);
        Button okButton = (Button) dialog.findViewById(R.id.dialog_ok);
        Button cancleButton = (Button) dialog.findViewById(R.id.dialog_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(responseBodyText.equals("Payment Successful")){
                    Intent intent = new Intent(BuyCreditActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    edtUserName.setText("");
                    edtBTrans.setText("");
                    dialog.dismiss();
                }


            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    class SubmitPaymantAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingDialog = ProgressDialog.show(BuyCreditActivity.this, "Please wait", "Loading...");
        }

        @Override
        protected String doInBackground(String... strings) {


            OkHttpClient client = new OkHttpClient();

            try {

                Request request = new Request.Builder()
                        .url(bKashAPI)

                        .build();


                Response response = null;

                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {


                    throw new IOException("Okhttp Error: " + response);

                } else {
                    responseBodyText = response.body().string();


                    //  JSONObject jobject = new JSONObject(responseBodyText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           // Toast.makeText(BuyCreditActivity.this,responseBodyText, Toast.LENGTH_SHORT).show();
                            openDialog(responseBodyText);
                        }
                    });

                }



            } catch (IOException e) {
                e.printStackTrace();
            }


            return responseBodyText;


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loadingDialog.dismiss();

        }
    }


    class CheckUserTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loadingDialog = ProgressDialog.show(BuyCreditActivity.this, "Please wait", "Loading...");
        }

        @Override
        protected String doInBackground(String... strings) {


            OkHttpClient client = new OkHttpClient();

            try {

                Request request = new Request.Builder()
                        .url(checkUserAPI)

                        .build();


                Response response = null;

                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {


                    throw new IOException("Okhttp Error: " + response);

                } else {
                    responseBodyText = response.body().string();
                    getUser = responseBodyText.replace("\n", "");

                }



            } catch (IOException e) {
                e.printStackTrace();
            }


            return responseBodyText;


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // loadingDialog.dismiss();

        }
    }
    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
