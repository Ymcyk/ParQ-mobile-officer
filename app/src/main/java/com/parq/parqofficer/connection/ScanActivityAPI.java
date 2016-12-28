package com.parq.parqofficer.connection;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parq.parqofficer.ScanActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                            JSONArray arrayResponse = new JSONArray(response);
                            if(arrayResponse.length() == 0){
                                scanActivity.onInvalidTicket();
                                return;
                            }

                            JSONObject vehicleResponse = arrayResponse
                                    .getJSONObject(0)
                                    .getJSONObject("vehicle");
                            JSONObject parkingResponse = arrayResponse
                                    .getJSONObject(0)
                                    .getJSONObject("parking");

                            String plateCountry = vehicleResponse.getString("plate_country");
                            String plateNumber = vehicleResponse.getString("plate_number");
                            String parkingName = parkingResponse.getString("name");

                            Ticket ticket = new Ticket(plateCountry, plateNumber, parkingName);
                            scanActivity.onValidTicket(ticket);

                        } catch (JSONException e) {
                            scanActivity.onParseError();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse != null && error.networkResponse.statusCode == 406){
                            scanActivity.onInvalidTicket();
                            return;
                        }
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
