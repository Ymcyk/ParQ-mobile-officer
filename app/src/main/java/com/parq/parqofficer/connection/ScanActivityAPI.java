package com.parq.parqofficer.connection;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parq.parqofficer.App;
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

    public ScanActivityAPI(ScanActivity scanActivity) {
        this.scanActivity = scanActivity;
    }

    public void requestTicket(final String badge) {
        StringRequest ticketRequest = new StringRequest(
                Request.Method.GET,
                App.getUrl().getTicketByBadgeURL(badge),
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
                            scanActivity.connectionError(App.PARSE_ERROR);
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
                        scanActivity.connectionError(App.CONNECTION_ERROR);
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", String.format("Token %s", App.getToken()));
                return headers;
            }
        };

        ticketRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                        1,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        Volley.newRequestQueue(scanActivity).add(ticketRequest);
    }
}
