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
    private TextView validityLabel;
    private TextView plateLabel;
    private TextView parkingLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        validityLabel = (TextView) findViewById(R.id.validity_label);
        plateLabel = (TextView) findViewById(R.id.plate_label);
        parkingLabel = (TextView) findViewById(R.id.parking_label);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cleanLabels();
    }

    public void startScanOnClick(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
                .setPrompt("Scan")
                .setOrientationLocked(true);
        integrator.initiateScan();
    }

    public void onValidTicket(Ticket ticket){
        validityLabel.setText("Bilet jest ważny");
        plateLabel.setText(String.format("%s-%s", ticket.getPlateCountry(), ticket.getPlateNumber()));
        parkingLabel.setText(ticket.getParking());
    }

    public void onInvalidTicket() {
        validityLabel.setText("Brak biletu");
    }

    public void badBadgeId() {
        validityLabel.setText("Brak biletu");
        Toast.makeText(this, "Nieodpowiedni kod QR", Toast.LENGTH_LONG).show();
    }

    private void cleanLabels() {
        validityLabel.setText("");
        plateLabel.setText("");
        parkingLabel.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && result.getContents() != null){
            //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            sendRequest(result.getContents());
        }
    }

    private void sendRequest(String badge) {
        ScanActivityAPI api = new ScanActivityAPI(this);
        api.requestTicket(badge);
    }

    public void connectionError(int errorCode) {
        switch (errorCode){
            case App.PARSE_ERROR:
                Toast.makeText(this, "Błąd podczas parsowania", Toast.LENGTH_LONG).show();
                break;
            case App.CONNECTION_ERROR:
                Toast.makeText(this, "Błąd połączenia", Toast.LENGTH_LONG).show();
                break;
            case App.UNAUTHENTICATED:
                Toast.makeText(this, "Brak uprawnień", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
