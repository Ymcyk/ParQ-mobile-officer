package com.parq.parqofficer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.parq.parqofficer.connection.ScanActivityAPI;
import com.parq.parqofficer.connection.Ticket;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    public void startScanOnClick(View view) {
        ScanActivityAPI api = new ScanActivityAPI(this);
        api.requestTicket("18a131b1-7ff0-412b-8d6a-65b30c8e3ede");
        /*
        IntentIntegrator integrator = new IntentIntegrator(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setPrompt("Scan")
                .setOrientationLocked(true);
        integrator.initiateScan();
        */
    }

    public void onValidTicket(Ticket ticket){
        Toast.makeText(
                this,
                String.format("%s %s %s", ticket.getPlateCountry(), ticket.getPlateNumber(), ticket.getParking()),
                Toast.LENGTH_LONG).show();
    }

    public void onInvalidTicket() {
        Toast.makeText(
                this,
                "Ticket is not valid",
                Toast.LENGTH_LONG).show();
    }

    public void onConnectionError() {
        Toast.makeText(
                this,
                "Connection error",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && result.getContents() != null){
            Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
        }
    }
}
