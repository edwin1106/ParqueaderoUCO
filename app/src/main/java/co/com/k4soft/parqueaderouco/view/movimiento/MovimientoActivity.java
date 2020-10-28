package com.example.parqueaderouco.view.movimiento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.parqueaderouco.R;
import com.example.parqueaderouco.entidades.Movimiento;
import com.example.parqueaderouco.entidades.Tarifa;
import com.example.parqueaderouco.persistencia.room.DataBaseHelper;
import com.example.parqueaderouco.utilities.ActionBarUtil;
import com.example.parqueaderouco.utilities.DateUtil;

import java.text.ParseException;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovimientoActivity extends AppCompatActivity {

    private DataBaseHelper db;
    private ActionBarUtil actionBarUtil;
    @BindView(R.id.txtPlaca)
    public EditText txtPlaca;
    @BindView(R.id.tipoTarifaSpinner)
    public Spinner tipoTarifaSpinner;
    @BindView(R.id.btnIngreso)
    public Button btnIngreso;
    @BindView(R.id.btnSalida)
    public Button btnSalida;
    @BindView(R.id.layoutDatos)
    public ConstraintLayout layoutDatos;
    @BindView(R.id.txtCantidadHoras)
    public TextView txtCantidadHoras;
    @BindView(R.id.txtTotal)
    public TextView txtTotal;
    private List<Tarifa> tarifas;
    private Movimiento movimiento;
    private Tarifa tarifa;
    private String[] arrayTarifas;
    private String valorTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimiento);
        ButterKnife.bind(this);
        initComponents();
        hideComponents();
        cargarSpinner();
        spinnerOnItemSelected();
    }

    private void spinnerOnItemSelected() {
        tipoTarifaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tarifa = tarifas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cargarSpinner() {
        tarifas = db.getTarifaDAO().listar();
        if(tarifas.isEmpty()){
            Toast.makeText(getApplicationContext(), R.string.sin_tarifas,Toast.LENGTH_SHORT).show();
            finish();
        }else{
            arrayTarifas = new String[tarifas.size()];
            for (int i = 0; i<tarifas.size(); i++){
                arrayTarifas[i] = tarifas.get(i).getNombre();
                ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrayTarifas);
                tipoTarifaSpinner.setAdapter(arrayAdapter);
            }
        }
    }

    private void hideComponents() {
        tipoTarifaSpinner.setVisibility(View.GONE);
        btnIngreso.setVisibility(View.GONE);
        btnSalida.setVisibility(View.GONE);
        layoutDatos.setVisibility(View.GONE);
    }

    private void initComponents() {
        db = DataBaseHelper.getDBMainThread(this);
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.tarifas));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void buscarPlaca(View view) throws ParseException {
        hideComponents();
        movimiento =  db.getMovimientoDAO().findByPlaca(txtPlaca.getText().toString().toUpperCase());
       if(movimiento == null){
           ShowComponentesIngreso();
       }else {
           ShowComponentesSalida();
           CalculatePrice();
       }
    }

    private void CalculatePrice() throws ParseException {
        String fechaEntrada = movimiento.getFechaEntrada();
        String fechaSalida = DateUtil.DateToStringWithHour(new Date());
        movimiento.setFechaSalida(fechaSalida);

        int totalHoras = movimiento.horasTranscurridas();
        txtCantidadHoras.setText(Integer.toString(totalHoras) + " Hora(s)");
        Tarifa tarifaExistente = db.getTarifaDAO().getByIdTarifa(movimiento.getIdTarifa());
        valorTotal = String.valueOf(tarifaExistente.getPrecio()* totalHoras);
        txtTotal.setText(valorTotal);
    }

    private void ShowComponentesSalida() {
        btnSalida.setVisibility(View.VISIBLE);
        layoutDatos.setVisibility(View.VISIBLE);
    }

    private void ShowComponentesIngreso() {
        tipoTarifaSpinner.setVisibility(View.VISIBLE);
        btnIngreso.setVisibility(View.VISIBLE);
    }

    public void registrarIngreso(View view) {
        if(tarifa == null){
            Toast.makeText(getApplicationContext(), R.string.debe_seleccionar_tarifa, Toast.LENGTH_SHORT).show();
        }else if(movimiento == null){
            movimiento = new Movimiento();
            movimiento.setPlaca(txtPlaca.getText().toString().toUpperCase());
            movimiento.setIdTarifa(tarifa.getIdTarifa());
            movimiento.setFechaEntrada(DateUtil.DateToStringWithHour(new Date()));
            new InsercionMovimiento().execute(movimiento);
            movimiento = null;
            hideComponents();
        }
    }

    public void registrarSalida(View view)  {
        Movimiento movimientoExistente = db.getMovimientoDAO().findByPlaca(txtPlaca.getText().toString().toUpperCase());
        movimientoExistente.setFechaSalida(DateUtil.DateToStringWithHour(new Date()));
        movimientoExistente.setFinalizaMovimiento(true);
        movimientoExistente.setValorTotal(valorTotal);
        new ActualizarMovimiento().execute(movimientoExistente);
        movimientoExistente = null;
        valorTotal = "";
        hideComponents();
    }

    private class InsercionMovimiento extends AsyncTask<Movimiento, Void, Void>{

        @Override
        protected Void doInBackground(Movimiento... movimientos) {
            DataBaseHelper.getSimpleDB(getApplicationContext()).getMovimientoDAO().insert(movimientos[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), R.string.operacion_exitosa, Toast.LENGTH_SHORT).show();
        }
    }


    private class ActualizarMovimiento extends AsyncTask<Movimiento, Void, Void>
    {
        @Override
        protected Void doInBackground(Movimiento... movimientos) {
            DataBaseHelper.getSimpleDB(getApplicationContext()).getMovimientoDAO().update(movimientos[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(), getString(R.string.updateSuccess), Toast.LENGTH_LONG).show();
            super.onPostExecute(aVoid);
        }
    }
}
