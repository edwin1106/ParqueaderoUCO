package com.example.parqueaderouco.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.parqueaderouco.R;
import com.example.parqueaderouco.utilities.ActionBarUtil;
import com.example.parqueaderouco.view.movimiento.MovimientoActivity;
import com.example.parqueaderouco.view.movimiento.MovimientosActivity;

public class MainActivity extends AppCompatActivity {

    private ActionBarUtil actionBarUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }
    private void initComponents() {
        actionBarUtil = new ActionBarUtil(this);
        actionBarUtil.setToolBar(getString(R.string.menu_principal));
    }

    public void goToTarifaActivity(View view) {
        Intent intent = new Intent(this,TarifaActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void goToIngresoSalida(View view) {
        Intent intent = new Intent(this, MovimientoActivity.class);
        startActivity(intent);
    }

    public void goToMovimientosActivity(View view) {
        Intent intent = new Intent(this, MovimientosActivity.class);
        startActivity(intent);
    }
}
