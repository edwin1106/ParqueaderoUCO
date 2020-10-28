package com.example.parqueaderouco.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.parqueaderouco.R;
import com.example.parqueaderouco.entidades.Tarifa;
import com.example.parqueaderouco.persistencia.room.DataBaseHelper;
import com.example.parqueaderouco.utilities.ActionBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActualizacionTarifaActivity extends AppCompatActivity {

    @BindView(R.id.txtNombreTarifa)
    public EditText txtNombreTarifa;
    @BindView(R.id.txtValorTarifa)
    public EditText txtValorTarifa;
    private DataBaseHelper db;
    private Tarifa tarifaExistente;
    private ActionBarUtil actionBarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizacion_tarifa);
        ButterKnife.bind(this);
        initComponent();

    }

    private void initComponent() {
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.tarifas));
        db = DataBaseHelper.getDBMainThread(this);
        String tarifaId = getIntent().getStringExtra("Id");
        tarifaExistente = db.getTarifaDAO().getByIdTarifa(Integer.valueOf(tarifaId));
        txtNombreTarifa.setText(tarifaExistente.getNombre());
        txtValorTarifa.setText(String.valueOf(tarifaExistente.getPrecio()));
    }


    public void actualizar(View view) {
        String nombreTarifa = txtNombreTarifa.getText().toString();
        Double valorTarifa = ToDouble(txtValorTarifa.getText().toString());
        Integer tarifaId = tarifaExistente.getIdTarifa();

        if (validarInformacion(nombreTarifa, valorTarifa))
        {
            Tarifa tarifa = getTarifa(nombreTarifa, valorTarifa, tarifaId);
            new ActualizarTarifa().execute(tarifa);
            finish();
        }
    }

    private Tarifa getTarifa(String nombreTarifa, Double valorTarifa, int tarifaId) {
        Tarifa tarifa = new Tarifa();
        tarifa.setIdTarifa(tarifaId);
        tarifa.setNombre(nombreTarifa);
        tarifa.setPrecio(valorTarifa);
        return tarifa;
    }

    private boolean validarInformacion(String nombreTarifa, Double valorTarifa) {
        boolean esValido = true;
        if("".equals(nombreTarifa)){
            esValido = false;
            txtNombreTarifa.setError(getString(R.string.requerido));
        }

        if(valorTarifa== 0){
            esValido = false;
            txtValorTarifa.setError(getString(R.string.requerido));
        }

        return esValido;
    }

    private Double ToDouble(String valor) {
        return  "".equals(valor)?0:Double.parseDouble(valor);
    }

    private class ActualizarTarifa extends AsyncTask<Tarifa, Void, Void>
    {
        @Override
        protected Void doInBackground(Tarifa... tarifas) {
            DataBaseHelper.getSimpleDB(getApplicationContext()).getTarifaDAO().update(tarifas[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), getString(R.string.updateSuccess), Toast.LENGTH_LONG).show();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
