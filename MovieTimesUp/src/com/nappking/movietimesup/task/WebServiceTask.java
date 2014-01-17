package com.nappking.movietimesup.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.AsyncTask;
import android.util.Log;

public class WebServiceTask extends AsyncTask<String, Integer, String> {
	 
    public static final int POST_TASK = 1;
    public static final int GET_TASK = 2;
    public static final String URL = "http://movietimesup.gestores.cloudbees.net/rest/";
     
    private static final String TAG = "WebServiceTask";

    // connection timeout, in milliseconds (waiting to connect)
    private static final int CONN_TIMEOUT = 5000;
     
    // socket timeout, in milliseconds (waiting for data)
    private static final int SOCKET_TIMEOUT = 8000;
     
    private int taskType = GET_TASK;

    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

    public WebServiceTask(int taskType) {
        this.taskType = taskType;
    }

    public void addNameValuePair(String name, String value) {    	
        params.add(new BasicNameValuePair(name, value));
    }

    @Override
    protected void onPreExecute() { 
    }

    protected String doInBackground(String... urls) {
        String url = urls[0];
        String result = "";
        HttpResponse response = doResponse(url);
        if (response == null) {
            return result;
        } else {
            try {
                result = inputStreamToString(response.getEntity().getContent());
            } catch (IllegalStateException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {
    	//UpdateIDs
    	Log.i(TAG, response);
    }
     
    // Establish connection and socket (data retrieval) timeouts
    private HttpParams getHttpParams() {         
        HttpParams htpp = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);         
        return htpp;
    }
     
    private HttpResponse doResponse(String url) {         
        // Use our connection and data timeouts as parameters for our
        // DefaultHttpClient
        HttpClient httpclient = new DefaultHttpClient(getHttpParams());
        HttpResponse response = null;

        try {
            switch (taskType) {
            case POST_TASK:
                HttpPost httppost = new HttpPost(url);
                // Add parameters
                httppost.setEntity(new UrlEncodedFormEntity(params));
                response = httpclient.execute(httppost);
                break;
            case GET_TASK:
                HttpGet httpget = new HttpGet(url);
                response = httpclient.execute(httpget);
                break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return response;
    }
     
    private String inputStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        try {
            // Read response until the end
            while ((line = rd.readLine()) != null) {
                total.append(line);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        // Return full string
        return total.toString();
    }

}