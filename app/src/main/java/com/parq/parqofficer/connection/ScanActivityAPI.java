package com.parq.parqofficer.connection;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parq.parqofficer.R;
import com.parq.parqofficer.ScanActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by piotr on 28.12.16.
 */

public class ScanActivityAPI {
    private ScanActivity scanActivity;
    private ParQURLs url;

    public ScanActivityAPI(ScanActivity scanActivity) {
        this.scanActivity = scanActivity;
        this.url = new ParQURLs(LoginActivityAPI.getAuthority(), scanActivity);
    }

    public void requestTicket(final String badge) {
        StringRequest ticketRequest = new StringRequest(
                Request.Method.GET,
                url.getTicketByBadgeURL(badge),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject vehicleResponse = new JSONObject(response).getJSONObject("vehicle");
                            JSONObject parkingResponse = new JSONObject(response).getJSONObject("parking");

                            String plateCountry = vehicleResponse.getString("plate_country");
                            String plateNumber = vehicleResponse.getString("plate_number");
                            String parkingName = parkingResponse.getString("name");

                            Ticket ticket = new Ticket(plateCountry, plateNumber, parkingName);

                            scanActivity.onValidTicket(ticket);
                        } catch (JSONException e) {
                            scanActivity.onInvalidTicket();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            scanActivity.onConnectionError();
                            error.printStackTrace();
                    }
                }
        ) {
            // empty
        };

        Volley.newRequestQueue(scanActivity).add(ticketRequest);
    }

}
