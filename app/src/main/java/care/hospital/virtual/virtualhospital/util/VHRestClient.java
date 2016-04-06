package care.hospital.virtual.virtualhospital.util;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import care.hospital.virtual.virtualhospital.R;

/**
 * Created by fcm2009 on 4/6/16.
 */
public class VHRestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(R.string.api_url + url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(R.string.api_url + url, params, responseHandler);
    }
}
