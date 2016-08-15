package learnenglishhou.bluebirdaward.com.learne.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import learnenglishhou.bluebirdaward.com.learne.listener.JsonParserTaskListener;

/**
 * Created by SVTest on 21/07/2016.
 */

public class DownloadTextTask extends AsyncTask<String, Void, String> {

    // private int contentViewId;

    private String text;
    private String url;

    // Các dạng tồn tại của đối tượng gọi đến download text
    //
    private Activity activity;
    private Context context;
    private JsonParserTaskListener listener;
    //

    public DownloadTextTask(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        listener = (JsonParserTaskListener) context;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (!isNetworkOnline()) {
            return null;
        } else
            return downloadText(strings[0]);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onDownload();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s == null) {
            listener.onDisconnect();

        } else {
            listener.runLayout();
            listener.onDownloadTextSuccessed(s);
        }
    }

    /*
    TODO Xử lý kết nối mạng, tiến hàng downlaod
     */
    private InputStream openHTTPConnection(String urlString) {
        InputStream in = null;
        int response = -1;

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();

            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();

            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return in;
    }


    public String downloadText(String url) {
        InputStream in = null;
        in = openHTTPConnection(url);

        if (in == null)
            return null;

        BufferedReader f = new BufferedReader(new InputStreamReader(in));

        String read;
        StringBuilder sb = new StringBuilder();
        try {
            while ((read = f.readLine()) != null) {
                sb.append(read);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Log.d("MainABC", url);

        return sb.toString();
    }

    // Hết phần kết nối mạng và download

    // Kiểm tra kết nối
    public boolean isNetworkOnline() {
        boolean status = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getNetworkInfo(0);

        if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
            status = true;
        else {
            netInfo = cm.getNetworkInfo(1);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                status = true;
        }
        return status;
    }


}
