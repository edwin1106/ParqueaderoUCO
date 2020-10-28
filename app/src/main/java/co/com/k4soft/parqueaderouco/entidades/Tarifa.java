package com.example.parqueaderouco.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.parqueaderouco.persistencia.Tabla;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity(tableName = Tabla.TARIFA)
@NoArgsConstructor
public class Tarifa {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "idTarifa")
    private Integer idTarifa;
    @ColumnInfo(name = "nombre")
    private String nombre;
    @ColumnInfo(name = "precio")
    private double precio;

}