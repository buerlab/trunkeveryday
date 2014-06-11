package com.buerlab.returntrunk.net;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zhongqiling on 14-5-28.
 */
public class NetTask extends AsyncTask<String, Integer, String> {

    protected String doInBackground(String... parms){
        assert parms.length == 3;

        try{

            URL url = new URL(parms[0]);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(parms[1]);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = "";
            String currline = "";
            for(int i=0; (currline=reader.readLine())!=null; i++){
                result += currline;
            }
            Log.i("----GET RESULT FROM HTTP:", result);
            return result;

        }
        catch (Exception e){
            Log.d("error:", e.toString());
        }
        finally {
            Log.d("over","over");
            return "";
        }
    }

    protected void onPostExecute(String result){

    }
}
