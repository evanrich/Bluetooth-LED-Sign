/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.evanrich.android.BluetoothSignController;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * This is the main Activity that displays the current chat session.
 */
public class BluetoothChat extends Activity {

	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;

	// Debugging
	private static final String TAG = "BluetoothChat";
	private static final boolean D = true;

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	// define array for messages
	private final ArrayList<String> messages = new ArrayList<String>();
	public ArrayList<String> history = new ArrayList<String>();

	// Key names received from the BluetoothChatService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	// Intent request codes
	private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 3;

	// Layout Views
	private TextView mTitle;
	private ListView mConversationView;
	private EditText mOutEditText;
	private Button mSendButton;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private ArrayAdapter<String> mConversationArrayAdapter;
	// String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	private BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	private BluetoothChatService mChatService = null;
	public int loopstate = 0;
	public ToggleButton togglebutton;

	public Spinner ColorChoice;
	public String displaycolor = "d"; // color selection
	public final String Green = "d"; // ASCII code for "
	// public final String DisplayChar = "\t";
	public final String DisplayChar = "a";
	public final String Red = "e"; // ASCII code for #
	public final String Orange = "f"; // ASCII code for $
	String[] coloritems = new String[] { "Green", "Orange", "Red" };

	// Speed controls
	public int displayspeed = 40;
	public String speedchar = "t"; // empty variable to hold speed character
	public final String ten = "q"; // "q"
	public final String twenty = "r"; // "r"
	public final String thirty = "s"; // "s"
	public final String fourty = "t"; // "t"
	public final String fifty = "u"; // "u"
	public final String sixty = "v"; // "v"
	public final String seventy = "w"; // "w"
	public final String eighty = "x"; // "x"
	public final String ninety = "y"; // "y"
	public final String hundred = "z"; // "z"

	// Direction Controls

	public final String left = "o"; // Right -> Left
	public final String right = "p"; // Left -> Right
	public String msgdirection = "o";

	public void launchspeach(View view) {
		// Start voice Recognition
		startVoiceRecognitionActivity();
	}

	public void togglerepeat(View v) {
		String o;
		o = ".";
		history.add(o);
		sendMessage(o);
	}

	/**
	 * Fire an intent to start the speech recognition activity.
	 */
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Display Speech Input");
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		messages.clear();
		if (D)
			Log.e(TAG, "+++ ON CREATE +++");

		// Set up the window layout
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);

		// Set up the custom title
		mTitle = (TextView) findViewById(R.id.title_left_text);
		mTitle.setText(R.string.appshortname);
		mTitle = (TextView) findViewById(R.id.title_right_text);

		// Get local Bluetooth adapter
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		// If the adapter is null, then Bluetooth is not supported
		if (mBluetoothAdapter == null) {
			Toast toast = Toast.makeText(this, "Bluetooth is not available",
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER_HORIZONTAL
					| Gravity.CENTER_VERTICAL, 0, 0);
			toast.show();
			// finish();
			return;
		}

		// s = (Spinner) findViewById(R.id.colorselector);
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, coloritems);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// s.setAdapter(adapter);

		togglebutton = (ToggleButton) findViewById(R.id.repeat_toggle);
		togglebutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on clicks
				if (togglebutton.isChecked()) {
					loopstate = 1;
				} else {
					loopstate = 0;
				}
			}
		});

		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
				RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);

		if (activities.size() != 0) {

		} else {

		}
		;

		// setup listview for selections and when an item is clicked
		// send the text back to the text box.
		ListView lv = (ListView) findViewById(R.id.in);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// return the clicked item back to the textbox on main view
				EditText editText = (EditText) findViewById(R.id.edit_text_out);
				CharSequence intext = "";
				intext = ((TextView) view).getText();
				// remove the . from the end of the string
				editText.setText(intext, TextView.BufferType.EDITABLE);
			}
		});

	}

	@Override
	public void onStart() {
		super.onStart();
		if (D)
			Log.e(TAG, "++ ON START ++");

		// If BT is not on, request that it be enabled.
		// setupChat() will then be called during onActivityResult
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			// Otherwise, setup the chat session
		} else {
			if (mChatService == null)
				setupChat();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if (D)
			Log.e(TAG, "+ ON RESUME +");

		// Performing this check in onResume() covers the case in which BT was
		// not enabled during onStart(), so we were paused to enable it...
		// onResume() will be called when ACTION_REQUEST_ENABLE activity
		// returns.
		if (mChatService != null) {
			// Only if the state is STATE_NONE, do we know that we haven't
			// started already
			if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
				// Start the Bluetooth chat services
				mChatService.start();
			}
		}
	}

	private void setupChat() {
		Log.d(TAG, "setupChat()");

		// Initialize the array adapter for the conversation thread
		mConversationArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.message);
		mConversationView = (ListView) findViewById(R.id.in);
		mConversationView.setAdapter(mConversationArrayAdapter);

		// Initialize the compose field with a listener for the return key
		mOutEditText = (EditText) findViewById(R.id.edit_text_out);
		mOutEditText.setOnEditorActionListener(mWriteListener);

		// Initialize the send button with a listener that for click events
		mSendButton = (Button) findViewById(R.id.button_send);
		mSendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Send a message using content of the edit text widget
				TextView view = (TextView) findViewById(R.id.edit_text_out);
				String message = view.getText().toString();
				sendMessage(message);
			}
		});

		// Initialize the BluetoothChatService to perform bluetooth connections
		mChatService = new BluetoothChatService(this, mHandler);

		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

	@Override
	public synchronized void onPause() {
		super.onPause();
		if (D)
			Log.e(TAG, "- ON PAUSE -");
	}

	@Override
	public void onStop() {
		super.onStop();
		if (D)
			Log.e(TAG, "-- ON STOP --");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop the Bluetooth chat services
		if (mChatService != null)
			mChatService.stop();
		if (D)
			Log.e(TAG, "--- ON DESTROY ---");
	}

	private void ensureDiscoverable() {
		if (D)
			Log.d(TAG, "ensure discoverable");
		if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(
					BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}

	/**
	 * Sends a message.
	 * 
	 * @param message
	 *            A string of text to send.
	 */
	private void sendMessage(String message) {

		// Check that we're actually connected before trying anything
		if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
			Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT)
					.show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			String repeatchar = "";
			// Get the message bytes and tell the BluetoothChatService to write

			if (togglebutton.isChecked()) {
				repeatchar = "b";
				// loopstate = 1;
			} else if (!togglebutton.isChecked()) {
				repeatchar = "c";
				// loopstate = 0;
			}

			message = repeatchar + displaycolor + speedchar + message
					+ DisplayChar;

			// message = message.toUpperCase() + ".";
			byte[] send = message.getBytes();
			mChatService.write(send);

			// Reset out string buffer to zero and clear the edit text field
			mOutStringBuffer.setLength(0);
			mOutEditText.setText(mOutStringBuffer);
		}
	}

	// The action listener for the EditText widget, to listen for the return key
	private final TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView view, int actionId,
				KeyEvent event) {
			// If the action is a key-up event on the return key, send the
			// message
			if (actionId == EditorInfo.IME_NULL
					&& event.getAction() == KeyEvent.ACTION_UP) {
				String message = view.getText().toString();
				sendMessage(message);
			}
			if (D)
				Log.i(TAG, "END onEditorAction");
			return true;
		}
	};

	// The Handler that gets information back from the BluetoothChatService
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if (D)
					Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BluetoothChatService.STATE_CONNECTED:
					mTitle.setText(R.string.title_connected_to);
					mTitle.append(mConnectedDeviceName);
					mConversationArrayAdapter.clear();
					break;
				case BluetoothChatService.STATE_CONNECTING:
					mTitle.setText(R.string.title_connecting);
					break;
				case BluetoothChatService.STATE_LISTEN:
				case BluetoothChatService.STATE_NONE:
					mTitle.setText(R.string.title_not_connected);
					break;
				}
				break;
			case MESSAGE_WRITE:
				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
				String writeMessage = new String(writeBuf);

				// mConversationArrayAdapter.add("Me:  " + writeMessage);
				if (!(history.contains(writeMessage.substring(3,
						writeMessage.length() - 1)))) { // subtract the period
														// we're using as a line
														// terminator from the
														// history

					mConversationArrayAdapter.add(writeMessage.substring(3,
							writeMessage.length() - 1)); // subtract the period
															// we're using as a
															// line terminator
															// from the history
					history.add(writeMessage.substring(3,
							writeMessage.length() - 1)); // subtract the period
															// we're using as a
															// line terminator
															// from the history
				}
				break;
			// break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				// construct a string from the valid bytes in the buffer
				String readMessage = new String(readBuf, 0, msg.arg1);
				mConversationArrayAdapter.add(mConnectedDeviceName + ":  "
						+ readMessage);
				break;
			case MESSAGE_DEVICE_NAME:
				// save the connected device's name
				mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
				Toast.makeText(getApplicationContext(),
						"Connected to " + mConnectedDeviceName,
						Toast.LENGTH_SHORT).show();
				break;
			case MESSAGE_TOAST:
				Toast.makeText(getApplicationContext(),
						msg.getData().getString(TOAST), Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (D)
			Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
		case REQUEST_CONNECT_DEVICE_SECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, true);
			}
			break;
		case REQUEST_CONNECT_DEVICE_INSECURE:
			// When DeviceListActivity returns with a device to connect
			if (resultCode == Activity.RESULT_OK) {
				connectDevice(data, false);
			}
			break;
		case VOICE_RECOGNITION_REQUEST_CODE:
			// When Voice Recognizer returns results
			if (resultCode == RESULT_OK) {
				// Fill the list view with the strings the recognizer thought it
				// could have heard
				ArrayList<String> matches = new ArrayList<String>();
				matches = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				if (matches.size() > 0) {
					EditText editText = (EditText) findViewById(R.id.edit_text_out);
					editText.setText(matches.get(0).toUpperCase(),
							TextView.BufferType.EDITABLE);
				}
			}
			break;

		case REQUEST_ENABLE_BT:
			// When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
				// Bluetooth is now enabled, so set up a chat session
				setupChat();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "BT not enabled");
				Toast.makeText(this, R.string.bt_not_enabled_leaving,
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

	private void connectDevice(Intent data, boolean secure) {
		// Get the device MAC address
		String address = data.getExtras().getString(
				DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		// Get the BLuetoothDevice object
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		// Attempt to connect to the device
		mChatService.connect(device, secure);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent serverIntent = null;
		switch (item.getItemId()) {
		case R.id.secure_connect_scan:
			// Launch the DeviceListActivity to see devices and do scan
			serverIntent = new Intent(this, DeviceListActivity.class);
			startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
			return true;
			// case R.id.insecure_connect_scan:
			// // Launch the DeviceListActivity to see devices and do scan
			// serverIntent = new Intent(this, DeviceListActivity.class);
			// startActivityForResult(serverIntent,
			// REQUEST_CONNECT_DEVICE_INSECURE);
			// return true;
		case R.id.settings:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View layout = inflater.inflate(R.layout.messsageoptions,
					(ViewGroup) findViewById(R.id.root));
			final Spinner ColorChoice = (Spinner) layout
					.findViewById(R.id.spinColor);
			final SeekBar MessageSpeed = (SeekBar) layout
					.findViewById(R.id.seekBar1);
			final TextView SpeedValue = (TextView) layout
					.findViewById(R.id.txtSpeed);

			// pre-set color choice to whatever is currently selected on the
			// main form.
			if (displaycolor == Green) {
				ColorChoice.setSelection(0);
			} else if (displaycolor == Orange) {
				ColorChoice.setSelection(1);
			} else if (displaycolor == Red) {
				ColorChoice.setSelection(2);
			}
			// Display current value in the box.
			MessageSpeed.setProgress((displayspeed / 10) - 1);
			SpeedValue.setText(String.valueOf(displayspeed) + " ms");

			MessageSpeed
					.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
						@Override
						public void onProgressChanged(SeekBar seekBar,
								int progress, boolean fromUser) {
							// TODO Auto-generated method stub
							int speed = (progress + 1) * 10;
							displayspeed = speed;

							switch (displayspeed) {
							case 10:
								speedchar = ten;
								break;
							case 20:
								speedchar = twenty;
								break;
							case 30:
								speedchar = thirty;
								break;
							case 40:
								speedchar = fourty;
								break;
							case 50:
								speedchar = fifty;
								break;
							case 60:
								speedchar = sixty;
								break;
							case 70:
								speedchar = seventy;
								break;
							case 80:
								speedchar = eighty;
								break;
							case 90:
								speedchar = ninety;
								break;
							case 100:
								speedchar = hundred;
								break;
							}
							SpeedValue.setText(String.valueOf(speed) + " ms");
						}

						@Override
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub

						}
					});

			// TODO: Create Dialog here and return it (see subsequent steps)
			AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
			builder2.setTitle("Message Options");
			builder2.setView(layout);
			builder2.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							if (ColorChoice.getSelectedItemPosition() == 0) {
								displaycolor = Green;
							} else if (ColorChoice.getSelectedItemPosition() == 1) {
								displaycolor = Orange;
							} else if (ColorChoice.getSelectedItemPosition() == 2) {
								displaycolor = Red;
							}
						}
					});
			builder2.show();

			return true;

		case R.id.discoverable:
			// Ensure this device is discoverable by others
			ensureDiscoverable();
			return true;
		case R.id.about:
			AlertDialog builder;
			try {
				builder = AboutDialogBuilder.create(this);
				builder.show();
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		case R.id.exit:
			finish();
			return true;

		}
		return false;
	}

}
