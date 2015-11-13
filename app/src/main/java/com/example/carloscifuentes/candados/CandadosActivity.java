package com.example.carloscifuentes.candados;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by carloscifuentes on 15-10-15.
 */
public class CandadosActivity extends AppCompatActivity {

    Button btnCandado;
    String Sobrecalentador;
    TextView tvSobrecalentador;
    ListView listView2;

    int IDCalentadores;
    int Niveles;
    int Paneles;
    int Tubos;

    ProgressBar progressBar3;
    String[] ListaDatos = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_candados);

        tvSobrecalentador = (TextView)findViewById(R.id.textView6);

        Sobrecalentador = getIntent().getExtras().getString("IDsobrecalentador");
        listView2 = (ListView)findViewById(R.id.listView2);

        progressBar3 = (ProgressBar)findViewById(R.id.progressBar3);


        IDCalentadores = getIntent().getExtras().getInt("Idcalentadores");
        Niveles = getIntent().getExtras().getInt("Niveles");
        Paneles = getIntent().getExtras().getInt("Paneles");
        Tubos = getIntent().getExtras().getInt("Tubos");

        //Toast.makeText(getApplicationContext(), "  IDCalentadores : " + IDCalentadores[0], Toast.LENGTH_LONG).show();
        tvSobrecalentador.setText(Sobrecalentador);
        btnCandado = (Button)findViewById(R.id.btnCandado);
        btnCandado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent act = new Intent(CandadosActivity.this, DetalleActivity.class);
                act.putExtra("IDsobrecalentador", Sobrecalentador);
                act.putExtra("Idcalentadores", IDCalentadores);
                act.putExtra("Niveles", Niveles);
                act.putExtra("Paneles", Paneles);
                act.putExtra("Tubos", Tubos);
                startActivity(act);
            }
        });

        Ion.with(this)
                .load("http://gestioniqs.azurewebsites.net/Gestion/GetInsidenciasSuperheater")
                .setBodyParameter("id", String.valueOf(IDCalentadores))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        if (e != null) {
                            Toast.makeText(getApplicationContext(), "  Error al procesar la consulta. ", Toast.LENGTH_LONG).show();
                        } else {
                            JSONObject contendor = null;
                            try {
                                contendor = new JSONObject(String.valueOf(result));
                                JSONArray Insidencias = contendor.getJSONArray("Insidencias");
                                ListaDatos = new String[Insidencias.length()];
                                for (int i = 0; i < Insidencias.length(); i++) {
                                    String Nivel = Insidencias.getJSONObject(i).getString("Nivel");
                                    String Panel = Insidencias.getJSONObject(i).getString("Panel");
                                    String Tubos = Insidencias.getJSONObject(i).getString("Tubos");
                                    ListaDatos[i] = "N-" + Nivel + " P-" + Panel + " T/" + Tubos;
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CandadosActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, ListaDatos);
                                listView2.setAdapter(adapter);
                                progressBar3.setVisibility(View.INVISIBLE);
                                //Toast.makeText(MainActivity.this, "nombresCalentadores...." + nombresCalentadores + "IDCalentadores...." + IDCalentadores + "Niveles...." + Niveles + "Paneles...." + Paneles +"Tubos...." + Tubos , Toast.LENGTH_LONG).show();
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                int itemPosition = position;
                String itemValue = (String) listView2.getItemAtPosition(position);

                Toast.makeText(CandadosActivity.this, itemValue, Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed() {
        Intent act = new Intent(CandadosActivity.this, MainActivity.class);
        startActivity(act);
    }
}
