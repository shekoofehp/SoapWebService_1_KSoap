package com.shekoofeh.mysoapwebservice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;


//import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends Activity {
    private final String NAMESPACE = "http://www.webservicex.net/";
    //Change IP to your machine IP
    private final String URL = "http://www.webservicex.net/geoipservice.asmx";
    private final String SOAP_ACTION = "http://www.webservicex.net/";
    private final String  METHOD_NAME = "GetGeoIP";
    private String TAG = "Geooooooo";
    private static String responseJSON;


    ProgressBar pg;

    TextView resultView;

    //GSON object
   // Gson gson = new Gson();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Progress bar to be displayed until app gets web service response
        pg = (ProgressBar) findViewById(R.id.progressBar1);
        //AysnTask class to handle WS call as separate UI Thread
        AsyncWSCall task = new AsyncWSCall();
        resultView= (TextView)findViewById(R.id.resultView);
        task.execute("54.174.31.254");


    }



    //AsynTask class to handle WS call as separate UI Thread
    private class AsyncWSCall extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Log.i(TAG, "doInBackground");
            //Invoke web method 
            invokeJSONWS(params[0],METHOD_NAME);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute"+ responseJSON+"...");
            //Convert JSON response into String array using fromJSON method
            //Make the progress bar invisbile
            //Display the progress bar
            TextView resultView= (TextView)findViewById(R.id.resultView);
            resultView.setText(responseJSON);
            pg.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            //Display the progress bar
            pg.setVisibility(View.VISIBLE);
            resultView.setText(".....");


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate:");

            resultView.setText(".....");
        }

    }

    //Method which invoke web methods
    public void invokeJSONWS(String ipAddress, String methName) {
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, methName);
       
	   // Property which holds input parameters
		
        /*PropertyInfo paramPI = new PropertyInfo();
        // Set Name
        paramPI.setName("IpAddress");
        // Set Value
        paramPI.setValue(ipAddress);
        // Set dataType
        paramPI.setType(String.class);
       
	   // Add the property to request object
        request.addProperty(paramPI);*/
        request.addProperty("IPAddress",ipAddress);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            Log.i(TAG, "calling....");
            // Invoke web service
            androidHttpTransport.call(SOAP_ACTION+methName, envelope);
            Log.i(TAG, "after call....");
            // Get the response
         //   SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            SoapObject response = (SoapObject) envelope.getResponse();

            // Assign it to static variable

            responseJSON = response.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
