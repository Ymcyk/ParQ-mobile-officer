package com.parq.parqofficer.connection;

import android.net.Uri.Builder;

/**
 * Created by piotr on 27.12.16.
 */

public class ParQURLs {
    private static String authority;

    public static String getAuthority() {
        return authority;
    }

    public static void setAuthority(String authority) {
        ParQURLs.authority = authority;
    }

    private static Builder getBase() {
        return new Builder()
                .scheme("http")
                .encodedAuthority(ParQURLs.authority);
    }

    public static String getLoginURL() {
        Builder builder = ParQURLs.getBase()
                .appendEncodedPath("api-token-auth/");
        return builder.build().toString();
    }
}
