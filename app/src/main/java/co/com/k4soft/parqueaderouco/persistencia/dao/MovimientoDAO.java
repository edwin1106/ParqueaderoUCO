package com.example.parqueaderouco.persistencia.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.parqueaderouco.entidades.Movimiento;
import com.example.parqueaderouco.entidades.Tarifa;

import java.util.List;

@Dao
public interface MovimientoDAO {

    @Query("SELECT * FROM MOVIMIENTO WHERE placa=:placa AND finalizaMovimiento = 0")
    Movimiento findByPlaca(String placa);

    @Insert
    void insert(Movimiento movimiento);

    @Update
    void update(Movimiento movimiento);

    @Query("SELECT * FROM movimiento ORDER BY fechaEntrada ASC")
    List<Movimiento> listar();

    @Query("SELECT * FROM movimiento WHERE fechaEntrada>=:fechaInicial AND fechaEntrada<=:fechaFinal ")
    List<Movimiento> listarRango(String fechaInicial, String fechaFinal);
}
