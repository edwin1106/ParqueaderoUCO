package com.example.parqueaderouco.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parqueaderouco.R;
import com.example.parqueaderouco.entidades.Tarifa;
import com.example.parqueaderouco.persistencia.room.DataBaseHelper;
import com.example.parqueaderouco.utilities.ActionBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TarifaActivity extends AppCompatActivity {

    private ActionBarUtil actionBarUtil;
    @BindView(R.id.listViewTarifas)
    public ListView listViewTarifas;
    public List<Tarifa> listaTarifas;
    DataBaseHelper db;

    public void goToRegistroTarifa(View view) {
        Intent intent = new Intent(this, RegistroTarifaActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarifa);
        ButterKnife.bind(this);
        initComponents();
        loadTarifas();
    }

    private void loadTarifas() {
        listaTarifas = db.getTarifaDAO().listar();
        if (listaTarifas.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.sin_tarifas, Toast.LENGTH_SHORT).show();
        } else {
            String[] tarifasArray = new String[listaTarifas.size()];
            for (int i = 0; i < listaTarifas.size(); i++) {
                tarifasArray[i] = listaTarifas.get(i).getNombre();
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, tarifasArray);
            listViewTarifas.setAdapter(arrayAdapter);
        }
    }


    private void initComponents() {
        db = DataBaseHelper.getDBMainThread(this);
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.tarifas));
        onItemClickListener();
    }

    private void onItemClickListener() {
        listViewTarifas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ActualizacionTarifaActivity.class);
                intent.putExtra("Id", getTarifaId(position));
                startActivity(intent);
            }

            private String getTarifaId(int position) {
                return String.valueOf(listaTarifas.get(position).getIdTarifa());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        loadTarifas();
    }
}
