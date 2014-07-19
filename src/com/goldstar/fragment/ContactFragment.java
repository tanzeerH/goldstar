package com.goldstar.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.goldstar.R;
import com.goldstar.adapter.ContactListadapter;
import com.goldstar.model.Contact;
import com.goldstar.utils.InvitePost;
import com.goldstar.utils.Utils;

public class ContactFragment extends Fragment {

	private ListView listView;
	private ContactListadapter cListAdapter;
	private ArrayList<Contact> contactList;
	ImageView imageView1, imageView2, imageView3, imageView4;
	Bitmap defaultUserPic;
	TextView tvNextStep;

	private static final int BUTTON_POSITIVE = -1;
	private static final int BUTTON_NEGATIVE = -2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_contacts, null, false);
		defaultUserPic = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.ic_contact_picture2);
		Log.e("msg", "contact_fragment");
		imageView1 = (ImageView) v.findViewById(R.id.iv1);
		imageView2 = (ImageView) v.findViewById(R.id.iv2);
		imageView3 = (ImageView) v.findViewById(R.id.iv3);
		imageView4 = (ImageView) v.findViewById(R.id.iv4);
		listView = (ListView) v.findViewById(R.id.listView1);
		tvNextStep = (TextView) v.findViewById(R.id.tvNextStep);
		contactList = getNameEmailDetails();
		cListAdapter = new ContactListadapter(getActivity(), R.layout.list_view_contact_row, contactList);
		listView.setAdapter(cListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				CheckBox chkBox = (CheckBox) v.findViewById(R.id.checkBox1);
				if (chkBox.isChecked()) {
					chkBox.setChecked(false);
					removeFromContactList(contactList.get(position).getContactId());
					invalidateImageList();

				} else {
					chkBox.setChecked(true);
					if (Utils.selectedContact.size() >= 4) {
						Toast.makeText(getActivity(), "4 friends already selected", Toast.LENGTH_LONG).show();
					} else {
						Utils.selectedContact.add(contactList.get(position));
						setImageInSelectedList(contactList.get(position).getContactId());
					}
				}

			}
		});
		tvNextStep.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDeleteDialog();
			}
		});
		return v;
	}

	private void setImageInSelectedList(long Id) {
		int pos = Utils.selectedContact.size();
		Log.e("pos", "" + pos);
		if (pos == 1) {
			imageView1.setImageBitmap(getContactPhoto(Id));

		} else if (pos == 2) {
			imageView2.setImageBitmap(getContactPhoto(Id));

		} else if (pos == 3) {
			imageView3.setImageBitmap(getContactPhoto(Id));

		} else if (pos == 4) {
			imageView4.setImageBitmap(getContactPhoto(Id));

		}

	}

	private void setInvitepostAddresses() {
		ArrayList<String> list = new ArrayList<String>();
		Log.e("emails", "" + Utils.selectedContact.size());
		for (int i = 0; i < Utils.selectedContact.size(); i++) {
			list.add(Utils.selectedContact.get(i).getEmail());
		}
		InvitePost invitePost = new InvitePost();
		// String[] addresses=new String[list.size()];
		invitePost.addresses = list.toArray(new String[list.size()]);
		for (String s : invitePost.addresses)
			Log.e("emails", s);
	}

	private void removeFromContactList(long Id) {
		for (int i = 0; i < Utils.selectedContact.size(); i++) {
			if (Utils.selectedContact.get(i).getContactId() == Id) {
				Utils.selectedContact.remove(i);
			}

		}

	}

	private void invalidateImageList() {
		for (int i = 0; i < Utils.selectedContact.size(); i++) {
			if (i == 0) {
				imageView1.setImageBitmap(getContactPhoto(Utils.selectedContact.get(i).getContactId()));
			} else if (i == 1) {
				imageView2.setImageBitmap(getContactPhoto(Utils.selectedContact.get(i).getContactId()));
			} else if (i == 2) {
				imageView3.setImageBitmap(getContactPhoto(Utils.selectedContact.get(i).getContactId()));
			} else if (i == 3) {
				imageView4.setImageBitmap(getContactPhoto(Utils.selectedContact.get(i).getContactId()));
			}
		}
		for (int i = Utils.selectedContact.size(); i < 4; i++) {
			if (i == 0) {
				imageView1.setImageBitmap(null);
			} else if (i == 1) {
				imageView2.setImageBitmap(null);
			} else if (i == 2) {
				imageView3.setImageBitmap(null);
			} else if (i == 3) {
				imageView4.setImageBitmap(null);
			}
		}

	}

	public Bitmap getContactPhoto(long contactId) {
		if (contactId == -1)
			return defaultUserPic;
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = getActivity().getContentResolver().query(photoUri, new String[] { Contacts.Photo.PHOTO }, null,
				null, null);
		if (cursor == null) {
			return null;
		}
		try {
			Bitmap thumbnail = defaultUserPic;
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					thumbnail = BitmapFactory.decodeByteArray(data, 0, data.length);
				}
			}
			return thumbnail;
		} finally {
			cursor.close();
		}
	}

	public ArrayList<Contact> getNameEmailDetails() {
		ArrayList<Contact> list = new ArrayList<Contact>();	
		ArrayList<String> nameList=new ArrayList<String>();
		Context context = getActivity();
		ContentResolver cr = context.getContentResolver();
		String[] PROJECTION = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.PHOTO_ID, ContactsContract.CommonDataKinds.Email.DATA,
				ContactsContract.CommonDataKinds.Photo.CONTACT_ID };
		String order = "CASE WHEN " + ContactsContract.Contacts.DISPLAY_NAME + " NOT LIKE '%@%' THEN 1 ELSE 2 END, "
				+ ContactsContract.Contacts.DISPLAY_NAME + ", " + ContactsContract.CommonDataKinds.Email.DATA
				+ " COLLATE NOCASE";
		String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";
		Cursor cur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, PROJECTION, filter, null, null);
		if (cur.moveToFirst()) {
			do {
				// names comes in hand sometimes
				String name = cur.getString(1);
				String emlAddr = cur.getString(3);
				long contactId = cur.getLong(4);
				Log.e("email", name + emlAddr + contactId);
				if(!nameList.contains(name))
				{
					nameList.add(name);
					Contact contact = new Contact(name, contactId, emlAddr);
					list.add(contact);
				}
			} while (cur.moveToNext());
		}

		cur.close();
		return list;
	}

	private void showDeleteDialog() {

		AlertDialog Alert = new AlertDialog.Builder(getActivity()).create();
		Alert.setTitle("Send Invitation");
		Alert.setMessage("Email a Goldstar invitation to the friend's you have selected?");

		Alert.setButton(BUTTON_POSITIVE, "Send", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				setInvitepostAddresses();
				Utils.selectedContact.clear();
				dialog.cancel();

			}
		});

		Alert.setButton(BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();

			}
		});
		Alert.show();
	}

}
