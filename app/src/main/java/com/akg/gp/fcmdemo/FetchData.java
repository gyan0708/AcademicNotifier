package com.akg.gp.fcmdemo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by GP on 10-Feb-17.
 */

public class FetchData extends AsyncTask<String, Void, String> {
    public String type_of_request;
    private String fetchedData;
    NetworkResponseListener listener;
    private String urlString;
    private URL url;
    private HttpURLConnection urlConnection;
    Context mContext;

    public FetchData(NetworkResponseListener parserListener, Context context) {
        this.listener = parserListener;
        this.mContext = context;
    }

    public void setType_of_request(String type_of_request) {
        this.type_of_request = type_of_request;
    }

    public void setData(JSONObject JSONData) throws UnsupportedEncodingException, JSONException {
        fetchedData = convertString(JSONData);
        //    Toast.makeText(mContext,"FInal String "+fetchedData,Toast.LENGTH_SHORT).show();
        Log.d("Final String", "  " + fetchedData);
    }

    public void setUrl(String url) {
        this.urlString = url;
    }


    public String convertString(JSONObject jsonObject) throws JSONException, UnsupportedEncodingException {
        String data = "";
        Iterator key = jsonObject.keys();
        while (key.hasNext()) {
            String k = key.next().toString();
            System.out.println("Key : " + k + ", value : "
                    + jsonObject.getString(k));
            if (!data.equals("")) {
                data = data + "&" + URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(jsonObject.getString(k), "UTF-8");
            } else {
                data = URLEncoder.encode(k, "UTF-8") + "=" + URLEncoder.encode(jsonObject.getString(k), "UTF-8");
            }
        }
        return data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.preRequest();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(type_of_request);
            if (type_of_request.equals(Config.POST)) {
                urlConnection.setDoOutput(true);
            }
            urlConnection.connect();
            if (type_of_request.equals(Config.POST)) {
                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                wr.write(fetchedData);
                wr.flush();
            }
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            String jsonResponse = buffer.toString();
            Log.d("Got response ", "Resposne is " + jsonResponse);
            return jsonResponse;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        listener.postRequest(response);
    }
}


