package com.example.parqueaderouco.entidades;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.parqueaderouco.persistencia.Tabla;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity(tableName = Tabla.MOVIMIENTO)
@NoArgsConstructor
public class Movimiento {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "idMovimiento")
    private Integer idMovimiento;
    @ColumnInfo(name = "idTarifa")
    private Integer idTarifa;
    @ColumnInfo(name = "placa")
    private String placa;
    @ColumnInfo(name = "fechaEntrada")
    private String fechaEntrada;
    @ColumnInfo(name = "fechaSalida")
    private String fechaSalida;
    @ColumnInfo(name = "finalizaMovimiento")
    private boolean finalizaMovimiento;
    @ColumnInfo(name = "valorTotal")
    private String valorTotal;

    public int horasTranscurridas() throws ParseException {
        String DATE_FORMAT_HORA = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_HORA, Locale.ENGLISH);
        Date entrada = dateFormat.parse(fechaEntrada);
        Date salida = dateFormat.parse(fechaSalida);

        final int milisegundos = 1000;
        int diferenciaEnSegundos = (int)(Math.abs(entrada.getTime() - salida.getTime()) / milisegundos);

        return calcularHoras(diferenciaEnSegundos);
    }

    private int calcularHoras(int diferencia) {
        final int segundosEnUnaHora = 3600;
        final int unaHora = 1;
        if (diferencia <= segundosEnUnaHora) return unaHora;
        return calcularHoras(diferencia - segundosEnUnaHora) + unaHora;
    }
}
