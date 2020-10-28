package com.example.parqueaderouco.persistencia.room;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.parqueaderouco.entidades.Movimiento;
import com.example.parqueaderouco.entidades.Tarifa;
import com.example.parqueaderouco.persistencia.dao.MovimientoDAO;
import com.example.parqueaderouco.persistencia.dao.TarifaDAO;


@Database(entities = {
        Tarifa.class,
        Movimiento.class}, version = DataBaseHelper.VERSION_BASE_DATOS, exportSchema = false)

public abstract class DataBaseHelper extends RoomDatabase {

    public static final int VERSION_BASE_DATOS = 5;
    public static final String NOMBRE_BASE_DATOS = "parqueadero";
    private static DataBaseHelper instace;


    public static DataBaseHelper getSimpleDB(Context context){
        if (instace == null) {
            instace = Room.databaseBuilder(context, DataBaseHelper.class, NOMBRE_BASE_DATOS).build();
        }
        return instace;
    }

    public static DataBaseHelper getDBMainThread(Context context){
        if (instace == null) {
            instace = Room.databaseBuilder(context, DataBaseHelper.class, NOMBRE_BASE_DATOS).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instace;
    }


    /**
     * Listado de DAO
     */

    public abstract TarifaDAO getTarifaDAO();

    public abstract MovimientoDAO getMovimientoDAO();


}