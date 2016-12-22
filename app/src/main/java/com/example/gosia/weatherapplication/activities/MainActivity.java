package com.example.gosia.weatherapplication.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gosia.weatherapplication.R;
import com.example.gosia.weatherapplication.objects.Weather;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String LOGGER = MainActivity.class.getSimpleName();

    private ListView mListView;
    private ArrayAdapter<Weather> mArrayAdapter;
    private BackgroundLoadingWeather mBackgroundLoadingWeather;
    private static ArrayList <Weather> mDataInfo = new ArrayList<Weather>();
    private static final String[] mCities = {"Warsaw", "Cracow", "Gdansk", "Bialystok", "Poznan", "Paris"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        initFields();
    }

    @Override
    public void onPause() {
        mDataInfo.clear();
        mArrayAdapter.notifyDataSetChanged();
    }

    public void loadData() {
        mBackgroundLoadingWeather = new BackgroundLoadingWeather("Poland");
        mBackgroundLoadingWeather.execute(mCities);
    }

    public void initFields() {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Weather selectedFromList = (Weather) (mListView.getItemAtPosition(position));

                Toast.makeText(getApplicationContext(), "for city:" + selectedFromList.getCity() + " in " + selectedFromList.getCountry() , Toast.LENGTH_LONG).show();
            }
        });

        mArrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, mDataInfo);
        mListView.setAdapter(mArrayAdapter);
    }

    public void refreshView(List<Weather> pWeathers) {
        Log.i(LOGGER,"refreshView");
        for(Weather weather : pWeathers) {
            if(weather != null) {
                addToArray(weather);
            } else {
                Log.w(LOGGER,"getTemperature/getHumidity()/getPressure() is null");
            }
        }

    }


    public void addToArray(Weather pWeatherInfo){
        mDataInfo.add(pWeatherInfo);
        mListView.invalidateViews();
    }






    public class BackgroundLoadingWeather extends AsyncTask<String, Void, List<Weather>> {

        private String mCountry;
        private Weather mWeatherObject;
        private String mAdress = "http://api.openweathermap.org/data/2.5/weather?q=Warsaw&APPID=c197531d48f9342edd4c5329d4f3d0a9"; //default value

        public Weather getWeatherObject() {
            return mWeatherObject;
        }

        public void createAdress(String pCity){
            mAdress = "http://api.openweathermap.org/data/2.5/weather?q=" + pCity +
                    "&APPID=c197531d48f9342edd4c5329d4f3d0a9";
        }


        public BackgroundLoadingWeather(String pCountry) {
            this.mCountry = pCountry;
        }

        @Override
        protected List<Weather> doInBackground(String... cities) {
            Log.i(LOGGER, "doInBackground");
            String text = null;
            JSONObject textJSON = null;
            ArrayList<Weather> weathers = new ArrayList<>();

            for(int i = 0; i < cities.length; i++) {
                String city = mCities[i];
                mBackgroundLoadingWeather.createAdress(city);

                URL url = null;
                try {
                    url = new URL(mAdress);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(url.openStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    text = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    textJSON = new JSONObject(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    mCountry = new JSONObject(textJSON.get("sys").toString()).getString("country");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String weather = null;
                try {
                    weather = textJSON.get("weather").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                weather = weather.substring(1, weather.length() - 1);
                try {
                    String description = (new JSONObject(weather)).getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject main = null;
                try {
                    main = (JSONObject) textJSON.get("main");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String temperature = null;
                try {
                    temperature = String.format("%.2f", main.getDouble("temp"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String temperature_min = null;
                try {
                    temperature_min = String.format("%.2f", main.getDouble("temp_min"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String temperature_max = null;
                try {
                    temperature_max = String.format("%.2f", main.getDouble("temp_max"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String humidity = null;
                try {
                    humidity = "" + main.getInt("humidity");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String pressure = null;
                try {
                    pressure = "" + (int) main.getDouble("pressure");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject wind = null;
                try {
                    wind = (JSONObject) textJSON.get("wind");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Double wind_speed = null;
                try {
                    wind_speed = wind.getDouble("speed");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mWeatherObject = new Weather(mCountry, city, temperature, temperature_min, temperature_max, humidity, pressure, wind_speed);
                Log.i(LOGGER,"mWeatherObject: " + mWeatherObject.toString());
                weathers.add(mWeatherObject);
            }

            return weathers;
        }

        protected void onPostExecute(List<Weather> pWeathers) {
            Log.i(LOGGER, "onPostExecute");
            if (pWeathers != null) {
                refreshView(pWeathers);
            } else {
                Log.e(LOGGER,"mWeatherObject is null");
            }
        }
    }
}
