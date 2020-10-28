package com.example.parqueaderouco.view.movimiento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.parqueaderouco.R;
import com.example.parqueaderouco.adapter.MovimientoAdapter;
import com.example.parqueaderouco.entidades.Movimiento;
import com.example.parqueaderouco.persistencia.room.DataBaseHelper;
import com.example.parqueaderouco.utilities.ActionBarUtil;
import com.example.parqueaderouco.utilities.DatePickerFragment;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovimientosActivity extends AppCompatActivity {

    @BindView(R.id.listViewMovimientos)
    public ListView listViewMovimientos;
    @BindView(R.id.txtFechaInicial)
    public EditText txtFechaInicial;
    @BindView(R.id.txtfechaFinal)
    public EditText txtFechaFinal;
    public List<Movimiento> listaMovimientos;
    DataBaseHelper db;
    private MovimientoAdapter movimientoAdapter;
    private ActionBarUtil actionBarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimientos);
        ButterKnife.bind(this);
        initComponents();
    }

    private void initComponents() {
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.movimientos));
        db = DataBaseHelper.getDBMainThread(this);
        onItemClickListener();

        txtFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaInicial);
            }
        });

        txtFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtFechaFinal);
            }
        });
    }


    private void onItemClickListener() {
        listViewMovimientos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),getMovimientoId(position), Toast.LENGTH_SHORT).show();
            }

            private String getMovimientoId(int position) {
                return String.valueOf(listaMovimientos.get(position).getIdMovimiento());
            }
        });
    }

    public void buscarPorRangoFechas(View view) {
        String fechaInicial = txtFechaInicial.getText().toString() + " " + "00:00:00";
        String fechaFinal = txtFechaFinal.getText().toString()  + " " + "24:59:59";
        if("".equals(txtFechaFinal.getText().toString()) || "".equals(txtFechaInicial.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Debe seleccionar ambas fechas", Toast.LENGTH_SHORT).show();

        }else {
            listaMovimientos = db.getMovimientoDAO().listarRango(fechaInicial, fechaFinal);
            movimientoAdapter = new MovimientoAdapter(this, listaMovimientos);
            listViewMovimientos.setAdapter(movimientoAdapter);
        }
    }

    public void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editText.setText(year + "-" + twoDigits(month+1) + "-" + twoDigits(day));
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

}
