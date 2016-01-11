package com.shekoofeh.mysoapwebservice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

//import com.google.gson.Gson;

public class Activity2 extends Activity {
    private final String NAMESPACE = "http://www.w3schools.com/webservices/";

    private final String URL = "http://www.w3schools.com/webservices/tempconvert.asmx";
    private final String SOAP_ACTION = "http://www.w3schools.com/webservices/";
    private final String METHODE_NAME = "CelsiusToFahrenheit";
    private String TAG = "Geooooooo";
    private static String responseJSON;

    ProgressBar pg;
    //GSON object;
   // Gson gson = new Gson();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Progress bar to be displayed until app gets web service response
        pg = (ProgressBar) findViewById(R.id.progressBar1);
        //AysnTask class to handle WS call as separate UI Thread
        AsyncWSCall task = new AsyncWSCall();
        task.execute("0", "Celsius");
        //task.execute("0", "Fahrenheit");
    }



    //AsynTask class to handle WS call as separate UI Thread
    private class AsyncWSCall extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            invokeJSONWS(params[0],params[1], METHODE_NAME );
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            //Convert JSON response into String array using fromJSON method
            //Make the progress bar invisbile
            pg.setVisibility(View.INVISIBLE);
            TextView resultView= (TextView)findViewById(R.id.resultView);
            resultView.setText(responseJSON);
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            //Display the progress bar
            pg.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate:" + responseJSON + "...");

        }

    }

    //Method which invoke web methods
    public void invokeJSONWS(String temperature, String paramName,String methName) {
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, methName);
        // Property which holds input parameters
        PropertyInfo paramPI = new PropertyInfo();
        // Set Name
        paramPI.setName(paramName);
        // Set Value
        paramPI.setValue(temperature);
        // Set dataType
        paramPI.setType(String.class);
        // Add the property to request object
        request.addProperty(paramPI);
       // request.addProperty("Celsius",temperature);
        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION+methName, envelope);
            // Get the response
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // Assign it to static variable
            responseJSON = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
