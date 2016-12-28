package com.parq.parqofficer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parq.parqofficer.connection.ScanActivityAPI;
import com.parq.parqofficer.connection.Ticket;

public class ScanActivity extends AppCompatActivity {
    TextView validityLabel;
    TextView plateLabel;
    TextView parkingLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        validityLabel = (TextView) findViewById(R.id.validity_label);
        plateLabel = (TextView) findViewById(R.id.plate_label);
        parkingLabel = (TextView) findViewById(R.id.parking_label);
    }

    public void startScanOnClick(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setPrompt("Scan")
                .setOrientationLocked(true);
        integrator.initiateScan();
    }

    public void onValidTicket(Ticket ticket){
        validityLabel.setText("Ticket is valid");
        plateLabel.setText(String.format("%s-%s", ticket.getPlateCountry(), ticket.getPlateNumber()));
        parkingLabel.setText(ticket.getParking());
    }

    public void onInvalidTicket() {
        validityLabel.setText("Ticket is invalid");
        plateLabel.setText("");
        parkingLabel.setText("");
    }

    public void onConnectionError() {
        validityLabel.setText("Connection error");
        plateLabel.setText("");
        parkingLabel.setText("");
    }

    public void onParseError() {
        validityLabel.setText("Parsing error");
        plateLabel.setText("");
        parkingLabel.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && result.getContents() != null){
            Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            sendRequest(result.getContents());
        }
    }

    private void sendRequest(String badge) {
        ScanActivityAPI api = new ScanActivityAPI(this);
        api.requestTicket(badge);
    }
}
