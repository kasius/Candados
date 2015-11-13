package com.example.carloscifuentes.candados;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar progressBar;
    TextView textView2;
    TextView textView13;
    TextView textView;
    TextView textView12;
    String[] nombresCalentadores = null;
    int[] IDCalentadores = null;
    int[] Niveles = null;
    int[] Paneles = null;
    int[] Tubos = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView13 = (TextView)findViewById(R.id.textView13);
        textView = (TextView)findViewById(R.id.textView);
        textView12 = (TextView)findViewById(R.id.textView12);
        textView12.setVisibility(View.INVISIBLE);
        textView13.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        Ion.with(MainActivity.this)
                .load("http://gestioniqs.azurewebsites.net/Gestion/GetSobrecalentadores")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // do stuff with the result or error
                        JSONObject contendor = null;
                        try {
                            contendor = new JSONObject(String.valueOf(result));
                            JSONArray calentadores = contendor.getJSONArray("CalentadoresDatos");
                            nombresCalentadores = new String[calentadores.length()];
                            IDCalentadores = new int[calentadores.length()];
                            Niveles        = new int[calentadores.length()];
                            Paneles        = new int[calentadores.length()];
                            Tubos          = new int[calentadores.length()];
                            for (int i = 0; i < calentadores.length(); i++) {
                                String name = calentadores.getJSONObject(i).getString("Nombre");
                                Integer id = calentadores.getJSONObject(i).getInt("Id");
                                Integer nivel = calentadores.getJSONObject(i).getInt("Niveles");
                                Integer panel = calentadores.getJSONObject(i).getInt("Paneles");
                                Integer tubo = calentadores.getJSONObject(i).getInt("Tubos");
                                nombresCalentadores[i] = name;
                                IDCalentadores[i]      = id;
                                Niveles[i]             = nivel;
                                Paneles[i]             = panel;
                                Tubos[i]               = tubo;
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, nombresCalentadores);
                            listView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);
                            textView2.setVisibility(View.INVISIBLE);
                            textView13.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            textView12.setVisibility(View.VISIBLE);
                            //Toast.makeText(MainActivity.this, "nombresCalentadores...." + nombresCalentadores + "IDCalentadores...." + IDCalentadores + "Niveles...." + Niveles + "Paneles...." + Paneles +"Tubos...." + Tubos , Toast.LENGTH_LONG).show();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                int itemPosition = position;
                String elemento = nombresCalentadores[itemPosition];
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);

                Intent act = new Intent(MainActivity.this, CandadosActivity.class);

                act.putExtra("IDsobrecalentador", itemValue);
                act.putExtra("Idcalentadores", IDCalentadores[itemPosition]);
                act.putExtra("Niveles", Niveles[itemPosition]);
                act.putExtra("Paneles", Paneles[itemPosition]);
                act.putExtra("Tubos", Tubos[itemPosition]);
                startActivity(act);
                // Show Alert
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
