package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private String tmp,day,date, location;
    private Handler handler;
    private  TextView tv_temp, tv_day, tv_date, tv_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        tv_temp = (TextView) findViewById(R.id.temperature_of_the_day);
        tv_day = (TextView) findViewById(R.id.current_day_of_the_week);
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_location =  (TextView)findViewById(R.id.tv_location);
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
    }

    public void btnRefresh(View view) {
        if (GetNetWorkState.getNetworkState(this)) {
            tv_temp.setText("loading");
            tv_day.setText("loading");
            tv_date.setText("loading");
            tv_location.setText("loading");
            new FetchWeather().execute();
        } else {
            Toast.makeText(this, "netwaork unavailable!", Toast.LENGTH_SHORT).show();
        }

    }

    private String getLocationFromJason(String jsonString) {
        Gson gson = new Gson();
        JasonData jasonData = gson.fromJson(jsonString,JasonData.class);
        return jasonData.getResult().getCitynm();
    }

    private String getDateFromJason(String jsonString) {
        Gson gson = new Gson();
        JasonData jasonData = gson.fromJson(jsonString,JasonData.class);
        return jasonData.getResult().getDays();
    }


    private String getTemperaturFromJason(String jsonString) {
        Gson gson = new Gson();
        JasonData jasonData = gson.fromJson(jsonString,JasonData.class);
        return jasonData.getResult().getTemperature_curr();
    }

    public String getDayFromJason(String jsonString) {
        Gson gson = new Gson();
        JasonData jasonData = gson.fromJson(jsonString,JasonData.class);
        return jasonData.getResult().getWeek();
    }


    private class FetchWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://api.k780.com:88/?app=weather.today&weaid=chongqing&&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonString) {

            tmp = getTemperaturFromJason(jsonString);
            day = getDayFromJason(jsonString);
            date = getDateFromJason(jsonString);
            location = getLocationFromJason(jsonString);

            tv_temp.setText(tmp);
            tv_day.setText(day);
            tv_date.setText(date);
            tv_location.setText(location);
        }
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://mpianatra.com/Courses/info.txt";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
        }
    }
}
