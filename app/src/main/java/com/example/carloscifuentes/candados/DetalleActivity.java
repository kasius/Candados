package com.example.carloscifuentes.candados;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by carloscifuentes on 15-10-15.
 */
public class DetalleActivity extends AppCompatActivity implements View.OnClickListener{

    Future<File> uploading;
    TextView textView6;
    TextView textView3;
    String Sobrecalentador;
    String StringImagen;
    ProgressBar progressBar2;
    TextView textView7;
    TextView textView11;
    TextView textView4;
    TextView textView5;
    TextView textView8;
    TextView textView9;

    int IDCalentadores;
    int Niveles;
    int Paneles;
    int Tubos;
    int valorTbos;

    Spinner spinner;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;

    CheckBox not;
    String Observacion;

    Button btnEnviar;

    ImageButton imgCapturaImagen;
    Intent i;
    final static int cons = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detalle);

        Sobrecalentador = getIntent().getExtras().getString("IDsobrecalentador");
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);
        textView7 = (TextView)findViewById(R.id.textView7);
        textView11 = (TextView)findViewById(R.id.textView11);
        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView8 = (TextView)findViewById(R.id.textView8);
        textView9 = (TextView)findViewById(R.id.textView9);

        progressBar2.setVisibility(View.INVISIBLE);
        textView7.setVisibility(View.INVISIBLE);

        IDCalentadores = getIntent().getExtras().getInt("Idcalentadores");
        Niveles = getIntent().getExtras().getInt("Niveles");
        Paneles = getIntent().getExtras().getInt("Paneles");
        Tubos = getIntent().getExtras().getInt("Tubos");

        valorTbos = Tubos;

        textView6=(TextView)findViewById(R.id.textView6);
        textView6.setText(Sobrecalentador);

        textView3=(TextView)findViewById(R.id.textView3);

        textView3.setGravity(Gravity.CENTER);

        imgCapturaImagen = (ImageButton)findViewById(R.id.imgCapturaImagen);

        btnEnviar =(Button)findViewById(R.id.btnEnviar);
        btnEnviar.setVisibility(View.INVISIBLE);

        spinner =(Spinner)findViewById(R.id.spinner);
        spinner2=(Spinner)findViewById(R.id.spinner2);
        spinner3=(Spinner)findViewById(R.id.spinner3);
        spinner4=(Spinner)findViewById(R.id.spinner4);

        List<Integer> listNiveles = new ArrayList<>();
        List<Integer> listPaneles = new ArrayList<>();
        List<Integer> listTubos = new ArrayList<>();
        List<String> listTipos = new ArrayList<>();

        listTipos.add("N/A");
        listTipos.add("Superior");
        listTipos.add("Inferior");

        List<String> listTubosString = new ArrayList<>();
        for(int i = 1;i <= Niveles; i++){
            listNiveles.add(i);
        }
        for(int i = 1;i<=Paneles;i++){
            listPaneles.add(i);
        }
        for(int i = 1;i<Tubos;i++){
            listTubosString.add(i+" - "+ (i+1));
            listTubos.add(i);
        }

        ArrayAdapter<Integer> dataAdapterNiveles = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, listNiveles);
        dataAdapterNiveles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapterNiveles);

        ArrayAdapter<Integer> dataAdapterPaneles = new ArrayAdapter<Integer>(this,
                android.R.layout.simple_spinner_item, listPaneles);
        dataAdapterPaneles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapterPaneles);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTubosString);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapterTipo = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listTipos);
        dataAdapterNiveles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(dataAdapterTipo);

        initCamara();

        not = (CheckBox)findViewById(R.id.checkBox);
        not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Observacion = "Observar";
                } else {
                    Observacion = "No Observar";
                }
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
    public void initCamara(){
        imgCapturaImagen = (ImageButton)findViewById(R.id.imgCapturaImagen);
        imgCapturaImagen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id;
        id = v.getId();
        switch (id) {
            case R.id.imgCapturaImagen:
                //upload.setVisibility(View.VISIBLE);
                if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, cons);
                    break;
                } else {
                    // no camera on this device
                    Toast.makeText(DetalleActivity.this, "Este dispositivo no posee camara", Toast.LENGTH_LONG).show();
                    break;
                }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        btnEnviar.setVisibility(View.VISIBLE);
        if (resultData != null) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToLast();
            String imagePath = cursor.getString(column_index_data);
            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
            imgCapturaImagen.setImageBitmap(bitmapImage );
            //por probar
            imgCapturaImagen.setRotation(90);
            imgCapturaImagen.getLayoutParams().height = 200;
            StringImagen = imagePath;

            //upload.setVisibility(View.VISIBLE);
            //imageView.setVisibility(View.VISIBLE);

        }
    }
    public void enviarDatosCandado(View v) {
        textView6.setVisibility(View.INVISIBLE);
        textView11.setVisibility(View.INVISIBLE);
        textView3.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        spinner2.setVisibility(View.INVISIBLE);
        spinner3.setVisibility(View.INVISIBLE);
        spinner4.setVisibility(View.INVISIBLE);
        textView8.setVisibility(View.INVISIBLE);
        textView4.setVisibility(View.INVISIBLE);
        textView5.setVisibility(View.INVISIBLE);
        imgCapturaImagen.setVisibility(View.INVISIBLE);
        btnEnviar.setVisibility(View.INVISIBLE);
        textView9.setVisibility(View.INVISIBLE);
        not.setVisibility(View.INVISIBLE);

        progressBar2.setVisibility(View.VISIBLE);
        textView7.setVisibility(View.VISIBLE);

        Spinner Nivel = (Spinner) findViewById(R.id.spinner);
        Spinner Panel = (Spinner) findViewById(R.id.spinner2);
        Spinner Tubos = (Spinner) findViewById(R.id.spinner3);
        Spinner Tipos = (Spinner) findViewById(R.id.spinner4);
        if (uploading != null && !uploading.isCancelled()) {
            resetUpload();
            return;
        }
        File f = new File(StringImagen);

        File echoedFile = getFileStreamPath("echo");

        //btnEnviar.setText("Cancel");
        String NivelText = Nivel.getSelectedItem().toString();
        String PanelText = Panel.getSelectedItem().toString();
        String TubosText = Tubos.getSelectedItem().toString();
        String TipoText  = Tipos.getSelectedItem().toString();

        // this is a 180MB zip file to test with
        uploading = Ion.with(DetalleActivity.this)
                .load("POST", "http://gestioniqs.azurewebsites.net/Gestion/GuardarCandado")
                .uploadProgressBar(progressBar2)
                .uploadProgressHandler(new ProgressCallback() {
                    @Override
                    public void onProgress(long downloaded, long total) {
                        textView7.setText("" + downloaded + " / " + total);
                    }
                })
                .setMultipartFile("largefile", "Content-Transfer-Encoding := BASE64", f)
                .setMultipartParameter("IDCalentador", String.valueOf(IDCalentadores))
                .setMultipartParameter("Nivel", NivelText)
                .setMultipartParameter("Panel", PanelText)
                .setMultipartParameter("Tubos", TubosText)
                .setMultipartParameter("Tipo", TipoText)
                .setMultipartParameter("Obs", Observacion)
                .write(echoedFile)
                //run a callback on completion
                .setCallback(new FutureCallback<File>() {
                    @Override
                    public void onCompleted(Exception e, File result) {
                        resetUpload();
                        if (e != null) {
                            Toast.makeText(DetalleActivity.this, "Error al enviar Notificación, intentelo mas tarde.", Toast.LENGTH_LONG).show();
                            textView6.setVisibility(View.VISIBLE);
                            textView11.setVisibility(View.VISIBLE);
                            textView3.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.VISIBLE);
                            spinner2.setVisibility(View.VISIBLE);
                            spinner3.setVisibility(View.VISIBLE);
                            textView4.setVisibility(View.VISIBLE);
                            textView5.setVisibility(View.VISIBLE);
                            imgCapturaImagen.setVisibility(View.VISIBLE);
                            btnEnviar.setVisibility(View.VISIBLE);

                            progressBar2.setVisibility(View.INVISIBLE);
                            textView7.setVisibility(View.INVISIBLE);
                            return;
                        }
                        Toast.makeText(DetalleActivity.this, "Información enviada a Sala de Control, Bien hecho!", Toast.LENGTH_LONG).show();
                        textView6.setVisibility(View.VISIBLE);
                        textView11.setVisibility(View.VISIBLE);
                        textView3.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        spinner2.setVisibility(View.VISIBLE);
                        spinner3.setVisibility(View.VISIBLE);
                        textView4.setVisibility(View.VISIBLE);
                        textView5.setVisibility(View.VISIBLE);
                        imgCapturaImagen.setVisibility(View.VISIBLE);
                        btnEnviar.setVisibility(View.VISIBLE);

                        progressBar2.setVisibility(View.INVISIBLE);
                        textView7.setVisibility(View.INVISIBLE);
                        Intent act = new Intent(DetalleActivity.this, CandadosActivity.class);
                        act.putExtra("IDsobrecalentador", Sobrecalentador);
                        act.putExtra("Idcalentadores", IDCalentadores);
                        act.putExtra("Niveles", Niveles);
                        act.putExtra("Paneles", Paneles);
                        act.putExtra("Tubos", valorTbos);
                        startActivity(act);
                    }
                });
    }

    void resetUpload() {
        //uploading.cancel();
        uploading = null;

        // reset the ui
        btnEnviar.setText("Upload");
        textView7.setText(null);
        progressBar2.setProgress(0);
    }
    @Override
    public void onBackPressed() {
        Intent act = new Intent(DetalleActivity.this, MainActivity.class);
        startActivity(act);
    }
}
