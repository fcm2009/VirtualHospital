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
    private final static String baseURL = "http://10.0.2.2:8080/";

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(baseURL + url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(baseURL + url, params, responseHandler);
    }
}
