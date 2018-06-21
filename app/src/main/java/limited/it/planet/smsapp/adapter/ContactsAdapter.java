package limited.it.planet.smsapp.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import limited.it.planet.smsapp.R;
import limited.it.planet.smsapp.model.ContactModel;

/**
 * Created by Tarikul on 6/7/2018.
 */

public class ContactsAdapter extends BaseAdapter {
    private ArrayList<ContactModel> mcontacts;
    private LayoutInflater mInflater;
    Context mContext;


    public ContactsAdapter(final ArrayList<ContactModel> contact, Context context) {
        this.mcontacts = contact;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);


    }


    @Override
    public int getCount() {
        return mcontacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mcontacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final ViewHolder holder;
        final View rootView;
        if (convertView == null) {
            holder = new ViewHolder();
            rootView = mInflater.inflate(R.layout.contact_list_item, parent, false);
            holder.contactsName = (TextView) rootView.findViewById(R.id.txv_user_name);
            holder.phoneNumber = (TextView) rootView.findViewById(R.id.txv_user_number);
            holder.listItemCheckBox = (CheckBox)rootView.findViewById(R.id.cb_select);

            rootView.setTag(holder);
        } else {
            rootView = convertView;
            holder = (ViewHolder) rootView.getTag();
        }


        ContactModel contact = mcontacts.get(position);
        String name = contact.getUserName();
        String phone = contact.getContactNumber();
        boolean isChecked = contact.isChecked();

        holder.contactsName.setText(name);
        holder.phoneNumber.setText(phone);
        holder.listItemCheckBox.setChecked(isChecked);

        return rootView;
    }




    // /////////// //
    // ViewHolder //
    // ///////// //
    private static class ViewHolder {
        TextView contactsName,phoneNumber;

        CheckBox listItemCheckBox;
    }





}
