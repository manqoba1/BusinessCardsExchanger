package codetribe.sifiso.com.businesscardsexchanger;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.Locale;

import codetribe.sifiso.com.bcelibrary.Models.PersonModel;

public class NFCActivity extends AppCompatActivity {

    private CardView bsCard;
    private FloatingActionButton fab;
    private boolean mResumed = false;
    private boolean mWriteMode = false;
    private TextView bsErrorHandlerText;
    private ImageView bsErrorHandlerIcon;

    private NfcAdapter mNfcAdapter;
    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;

    private PersonModel personModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        personModel = new PersonModel();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // bsCard.setVisibility(View.VISIBLE);
                if (mNfcAdapter != null) {
                    fab.hide();
                    enableNdefExchangeMode();
                }
            }
        });
        setFields();
        if (mNfcAdapter != null) {
            bsErrorHandlerText.setText("Ready to Exchange Business Cards");
            bsErrorHandlerIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.stat_notify_chat));
        } else {
            bsErrorHandlerText.setText("NFC not available");
            bsErrorHandlerIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), android.R.drawable.stat_notify_error));
        }
        ReadNFCTags();

    }

    private void setFields() {
        bsErrorHandlerIcon = (ImageView) findViewById(R.id.bsErrorHandlerIcon);
        bsErrorHandlerText = (TextView) findViewById(R.id.bsErrorHandlerText);
        bsCard = (CardView) findViewById(R.id.bsCard);
        bsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void ReadNFCTags() {

        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
        }
        mNdefExchangeFilters = new IntentFilter[]{ndefDetected};

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[]{tagDetected};
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        Log.d(TAG, "#### Start onResume : " + intent.toString());
        if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
        }

    }

    NdefMessage[] getNdefMessages(Intent intent) {
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record =
                        new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{
                        record
                });
                msgs = new NdefMessage[]{
                        msg
                };
            }
        } else {
            Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void promptForContent(final NdefMessage msg) {
        new AlertDialog.Builder(this).setTitle("New Friend?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String body = new String(msg.getRecords()[0].getPayload());
                        Log.d(TAG, body);
                        //save to the local storage
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).show();
    }

    private NdefMessage getNoteAsNdef() {
        return new NdefMessage(new NdefRecord[]{
                createNewTextRecord(personModel, Locale.ENGLISH, true)
        });
    }

    public NdefRecord createNewTextRecord(PersonModel personModel, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Log.d(TAG, "#### byte size : " + langBytes.length + " Text json : " + new Gson().toJson(personModel));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        String toJsonText = new Gson().toJson(personModel);
        byte[] textBytes = toJsonText.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    public void onResume() {
        super.onResume();
        mResumed = true;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            Log.d(TAG, "#### Start onResume");
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            bsErrorHandlerText.setText(new String(payload));
            setIntent(new Intent()); // Consume this intent.
        }
        if (mNfcAdapter != null) {
            enableNdefExchangeMode();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mResumed = false;
        disableNdefExchangeMode();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableNdefExchangeMode();
    }

    private void enableNdefExchangeMode() {
        Log.d(TAG, "#### Start The exchange");
        mNfcAdapter.enableForegroundNdefPush(NFCActivity.this, getNoteAsNdef());
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundNdefPush(this);
        mNfcAdapter.disableForegroundDispatch(this);
    }

    private String TAG = NFCActivity.class.getSimpleName();
}
