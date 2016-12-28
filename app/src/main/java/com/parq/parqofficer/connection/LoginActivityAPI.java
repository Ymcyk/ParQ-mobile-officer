package com.parq.parqofficer.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parq.parqofficer.R;
import com.parq.parqofficer.exceptions.NoApiURLException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by piotr on 28.12.16.
 */

public class LoginActivityAPI {
    private Context context;
    private SharedPreferences sharedPref;
    private ParQURLs url;
    private String token;

    public LoginActivityAPI(Context context){
        this.context = context;
        this.sharedPref = context.getSharedPreferences(
                this.context.getString(R.string.sharedpref_file_key),
                Context.MODE_PRIVATE);
        this.url = new ParQURLs(getURLFromSharedPref(), this.context);
    }

    public void login(final String username, final String password) {
        // final String url = "http://192.168.1.20:8000/api-token-auth/";
        System.out.println("MÓJ URLLLLLLLLLLLL: " + url);

        StringRequest loginRequest = new StringRequest(Request.Method.POST, url.getLoginURL(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("WSZEDŁĘ∞!!!!!!!!!!!");
                        try {
                            //JSONObject jsonResponse = new JSONObject(response).getJSONObject("form");
                            JSONObject jsonResponse = new JSONObject(response);
                            String token = jsonResponse.getString("token");
                            System.out.println(String.format("Token: %s", token));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error ListeneRRRRRRRRRRRRRRRRRR " + error.networkResponse.statusCode);
                        if(error.networkResponse.statusCode == 400){
                            Toast toast = Toast.makeText(
                                    context,
                                    context.getString(R.string.bad_login_or_pass),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                String data = String.format("username=%s&password=%s", username, password);
                return data.getBytes();
            }
        };

        Volley.newRequestQueue(context).add(loginRequest);
    }

    private String getURLFromSharedPref() {
        return sharedPref.getString(context.getString(R.string.sharedpref_url_slug), null);
    }
}


