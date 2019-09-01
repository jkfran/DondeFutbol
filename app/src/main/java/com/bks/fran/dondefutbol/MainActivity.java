package com.bks.fran.dondefutbol;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        TableLayout tableLayout = (TableLayout) this.findViewById(R.id.table);

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://jkfran.esy.es/dondefutbol/partidos.json")
                    .build();
            Response responses = null;
            try {
                responses = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String jsonData = responses.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            JSONArray Jarray = Jobject.getJSONArray("todos");

            for (int i = 0; i < Jarray.length(); i++) {
                JSONObject object = Jarray.getJSONObject(i);

                TextView textView = new TextView(this);
                textView.setText(object.getString("fecha"));

                tableLayout.addView(textView);
                JSONArray jsonArray = object.getJSONArray("partido");
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject objectChild = jsonArray.getJSONObject(j);
                    TableRow row = new TableRow(this);
                    //row.setGravity(Gravity.CENTER);
                    textView = new TextView(this);
                    textView.setTextSize(9);
                    textView.setText(objectChild.getString("hora") + " ");
                    row.addView(textView);
                    textView = new TextView(this);
                    textView.setTextSize(9);
                    String equipo1 = objectChild.getString("equipo1");
                    if (equipo1.length() > 19)
                        equipo1 = equipo1.substring(0, 20);
                    textView.setText(equipo1);
                    textView.setGravity(Gravity.RIGHT);
                    row.addView(textView);
                    textView = new TextView(this);
                    textView.setTextSize(9);
                    textView.setText(" - ");
                    textView.setGravity(Gravity.CENTER);
                    row.addView(textView);
                    textView = new TextView(this);
                    textView.setTextSize(9);
                    String equipo2 = objectChild.getString("equipo2");
                    if (equipo2.length() > 19)
                        equipo2 = equipo2.substring(0, 20);
                    textView.setText(equipo2);
                    row.addView(textView);
                    textView = new TextView(this);
                    textView.setTextSize(9);
                    textView.setText(" "+objectChild.getString("canal"));
                    row.addView(textView);
                    tableLayout.addView(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
