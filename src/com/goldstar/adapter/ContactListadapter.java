package com.goldstar.adapter;

import java.util.List;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.goldstar.R;
import com.goldstar.model.Contact;
import com.goldstar.utils.Utils;

public class ContactListadapter extends ArrayAdapter<Contact> {
	public Context context;
	Bitmap defaultUserPic;


	public ContactListadapter(Context context,
			int textViewResourceId, List<Contact> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		defaultUserPic = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_contact_picture2);
		// TODO Auto-generated constructor stub
	}

	private class ViewHolder {

		TextView name;
		//TextView phoneNumber;
		ImageView imageView;
		CheckBox chkbox;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_view_contact_row,
					null);
			holder = new ViewHolder();
			holder.name=(TextView)convertView.findViewById(R.id.tvName);
			//holder.phoneNumber=(TextView)convertView.findViewById(R.id.textViewNumber);
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.chkbox=(CheckBox)convertView.findViewById(R.id.checkBox1);
			convertView.setTag(holder);
			
		}
		else
			holder = (ViewHolder) convertView.getTag();
		//Log.v("msg","hello");
		Contact contact=getItem(position);
		holder.name.setText(contact.getName());
		holder.chkbox.setChecked(false);
		for (int i = 0; i < Utils.selectedContact.size(); i++) {
			if (Utils.selectedContact.get(i).getContactId() == contact.getContactId()) {
				holder.chkbox.setChecked(true);
			}

		}
		//holder.phoneNumber.setText(contact.getPhoneNumber());
		holder.imageView.setImageBitmap(getContactPhoto(contact.getContactId()));

		return convertView;

	}
	public Bitmap getContactPhoto(long contactId) {
		if (contactId == -1)
			return defaultUserPic;
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = context.getContentResolver().query(photoUri,
				new String[] { Contacts.Photo.PHOTO }, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			Bitmap thumbnail = defaultUserPic;
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					thumbnail = BitmapFactory.decodeByteArray(data, 0,
							data.length);
				}
			}
			return thumbnail;
		} finally {
			cursor.close();
		}
	}

}