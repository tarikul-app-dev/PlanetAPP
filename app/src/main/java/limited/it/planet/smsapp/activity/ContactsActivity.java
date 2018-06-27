package limited.it.planet.smsapp.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.adapter.ContactsAdapter;
import limited.it.planet.smsapp.model.ContactModel;

import static android.Manifest.permission.READ_CONTACTS;

public class ContactsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imgvHome;
    private static final int REQUEST_READ_CONTACTS = 444;
    private ListView mListView;
    private ProgressDialog pDialog;
    private Handler updateBarHandler;

    ArrayList<ContactModel> contactList;
    Cursor cursor;
    int counter;
    String name = "";
    String phoneNumber = "";
    Button btnExContacts;
    ContactsAdapter adapter;

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    FileWriter fileWriter = null;
    public static final int REQUEST_PERM_WRITE_STORAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        toolbar = (Toolbar)findViewById(R.id.toolbar_contacts_activity);
        setSupportActionBar(toolbar);
        imgvHome = (ImageView)findViewById(R.id.imgv_home);
        btnExContacts = (Button)findViewById(R.id.btn_ex_contacts);
       // btnUnselect = (Button)findViewById(R.id.btn_unSelect_all);

        imgvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this,MainActivity.class);
                startActivity(intent);
                ActivityCompat.finishAffinity(ContactsActivity.this);
            }
        });



        pDialog = new ProgressDialog(ContactsActivity.this);
        pDialog.setMessage("Reading contacts...");
        pDialog.setCancelable(false);
        pDialog.show();

        mListView = (ListView) findViewById(R.id.list);
        updateBarHandler = new Handler();

        // Since reading contacts takes more time, let's run it on a separate thread.
        new Thread(new Runnable() {

            @Override
            public void run() {
                getContacts();
            }
        }).start();

        // Set onclicklistener to the list item.
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //TODO Do whatever you want with the list data
                ContactModel contact = contactList.get(position);
                Toast.makeText(getApplicationContext(), "item clicked : \n" + contact.getContactNumber(), Toast.LENGTH_SHORT).show();
            }
        });

        btnExContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ContactsActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERM_WRITE_STORAGE);

                } else{
                    String filename = "allcontacts.csv";
                    File sdCardDir = Environment.getExternalStorageDirectory();
                    // the name of the file to export with

                    File saveFile = new File(sdCardDir, filename);
                    try {
                        fileWriter = new FileWriter(saveFile);
                        //Add a new line separator after the header

                        fileWriter.append(NEW_LINE_SEPARATOR);
                        for (ContactModel student : contactList) {
                            fileWriter.append(String.valueOf(student.getContactNumber()));
                            fileWriter.append(COMMA_DELIMITER);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {

                        try {
                            fileWriter.flush();
                            fileWriter.close();
                            Toast.makeText(ContactsActivity.this,"All Contacts Exported Successfully in CSV file format ", Toast.LENGTH_LONG).show();

                        } catch (IOException e) {

                            e.printStackTrace();

                        }


                    }
                }



            }
        });
//        btnSelectAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for(ContactModel contacts:contactList){
//                    contacts.setChecked(true);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });

//        btnUnselect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                for(ContactModel contacts:contactList){
//                    contacts.setChecked(false);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
   }


    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts();
            }
        }
    }

    public void getContacts() {

        if (!mayRequestContacts()) {
            return;
        }

        contactList = new ArrayList<>();



        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output;

        ContentResolver contentResolver = getContentResolver();

        cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {

            counter = 0;
            while (cursor.moveToNext()) {
                output = new StringBuffer();

                // Update the progress message
                updateBarHandler.post(new Runnable() {
                    public void run() {
                        pDialog.setMessage("Reading contacts : " + counter++ + "/" + cursor.getCount());
                    }
                });

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                // contactModel.setUserName(name);

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    output.append("\n First Name:" + name);

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                        // contactModel.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    // Read every email id associated with the contact
                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));

                        output.append("\n Email:" + email);

                    }

                    emailCursor.close();

                    String columns[] = {
                            ContactsContract.CommonDataKinds.Event.START_DATE,
                            ContactsContract.CommonDataKinds.Event.TYPE,
                            ContactsContract.CommonDataKinds.Event.MIMETYPE,
                    };

                    String where = ContactsContract.CommonDataKinds.Event.TYPE + "=" + ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                            " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " + ContactsContract.Data.CONTACT_ID + " = " + contact_id;

                    String[] selectionArgs = null;
                    String sortOrder = ContactsContract.Contacts.DISPLAY_NAME;

                    Cursor birthdayCur = contentResolver.query(ContactsContract.Data.CONTENT_URI, columns, where, selectionArgs, sortOrder);
                    Log.d("BDAY", birthdayCur.getCount() + "");
                    if (birthdayCur.getCount() > 0) {
                        while (birthdayCur.moveToNext()) {
                            String birthday = birthdayCur.getString(birthdayCur.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE));
                            output.append("Birthday :" + birthday);
                            Log.d("BDAY", birthday);
                        }
                    }
                    birthdayCur.close();
                }


                // Add the contact to the ArrayList
                String phNumber = getMeMyNumber(phoneNumber);
                contactList.add(new ContactModel(name,phNumber));
            }

            // ListView has to be updated using a ui thread
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adapter = new ContactsAdapter(contactList,ContactsActivity.this);
                    mListView.setAdapter(adapter);
                }
            });

            // Dismiss the progressbar after 500 millisecondds
            updateBarHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    pDialog.cancel();
                }
            }, 500);
        }
    }

    public static String getMeMyNumber(String number)
    {
        String out = number.replaceAll("[^0-9]", "") ;       //remove all the non numbers (brackets dashes spaces etc.) except the + signs

        return out;

    }
}
