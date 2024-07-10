package com.example.myapplication_hava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
//100 ama 75 aldım görselleri eklemediğim için şimdi ise ekledim.
    TextView weatherhere, humidityhere, temphere, speedhere, timehere, pressurehere, idhere;
    ImageView picturehere;
    String sehir[] = {"Eskisehir", "Ankara", "Bursa", "Konya", "Istanbul", "Bursa", "Izmir", "Adana", "Mersin"};
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picturehere = findViewById(R.id.imageView);
        weatherhere = findViewById(R.id.weather);
        humidityhere = findViewById(R.id.humidity);
        temphere = findViewById(R.id.temp);
        speedhere = findViewById(R.id.speedid);
        timehere = findViewById(R.id.timezone);
        pressurehere = findViewById(R.id.pressure);
        idhere = findViewById(R.id.sehirId);
        spinner = findViewById(R.id.sehirSpinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sehir);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
    }

    public void call(View view) {
        String city = spinner.getSelectedItem().toString();
        String apiKey = "d45282d1f7ea737e83b59bb14831616e";
        String Json_Url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        Log.d("API Request", "URL: " + Json_Url); // Log the URL to check it

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Json_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("API Response", "Response: " + response); // Log the response to check it
                try {
                    JSONObject resp_obj = new JSONObject(response);

                    JSONArray weather_array = resp_obj.getJSONArray("weather");
                    JSONObject weather_obj = weather_array.getJSONObject(0);
                    String weather_json = weather_obj.getString("description");
                    weatherhere.setText(weather_json);
                    // Hava durumuna göre resim ayarlama
                    switch (weather_json.toLowerCase()) {
                        case "clear sky":
                        case "sunny":
                            picturehere.setImageResource(R.drawable.a01d2x);
                            break;
                        case "few clouds":
                        case "scattered clouds":
                        case "broken clouds":
                        case "cloudy":
                            picturehere.setImageResource(R.drawable.a02d2x);
                            break;
                        case "shower rain":
                        case "rain":
                        case "light rain":
                            picturehere.setImageResource(R.drawable.a09d2x);
                            break;
                        case "thunderstorm":
                            picturehere.setImageResource(R.drawable.a11d2x);
                            break;
                        case "snow":
                            picturehere.setImageResource(R.drawable.a13d2x);
                            break;
                        default:
                            picturehere.setImageResource(R.drawable.a50d2x); // Bilinmeyen hava durumu için varsayılan resim
                            break;
                    }
                    JSONObject wind_obj = resp_obj.getJSONObject("wind");
                    String speed = wind_obj.getString("speed");
                    speedhere.setText(speed);

                    JSONObject main_obj = resp_obj.getJSONObject("main");
                    String press = main_obj.getString("pressure");
                    pressurehere.setText(press);

                    String hum = main_obj.getString("humidity");
                    humidityhere.setText(hum);

                    String temp = main_obj.getString("temp");
                    temphere.setText(temp);

                    String idd = resp_obj.getString("id");
                    idhere.setText(idd);

                    String time = resp_obj.getString("timezone");
                    timehere.setText(time);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("API Error", "JSON Parsing Error: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("API Error", "Volley Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
