package com.parq.parqofficer.connection;

import android.content.Context;
import android.net.Uri.Builder;
import com.parq.parqofficer.R;

/**
 * Created by piotr on 27.12.16.
 */

public class ParQURLs {
    private String authority;
    private Context context;

    public ParQURLs(String authority, Context context) {
        this.authority = authority;
        this.context = context;
    }

    private Builder getBase() {
        return new Builder()
                .scheme("http")
                .encodedAuthority(authority);
    }

    public String getLoginURL() {
        Builder builder = getBase()
                .appendEncodedPath(context.getString(R.string.url_login));
        return builder.build().toString();
    }

    public String getCurrentURL() {
        Builder builder = getBase()
                .appendEncodedPath(context.getString(R.string.url_current));
        return builder.build().toString();
    }
}