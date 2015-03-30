package ordanel.ednom.DAO;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.util.Log;

import org.apache.http.impl.io.AbstractSessionInputBuffer;

import java.util.ArrayList;

import ordanel.ednom.Entity.CargoE;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Entity.PersonalE;
import ordanel.ednom.Library.ConstantsUtils;

public class PersonalDAO extends BaseDAO {
    private static final String TAG = PersonalDAO.class.getSimpleName();
    private static PersonalDAO personalDAO;
    PersonalE personalE;

    ArrayList<PersonalE> personalEArrayList;

    public synchronized static PersonalDAO getInstance(Context paramContext){
        if (personalDAO == null){
            personalDAO = new PersonalDAO(paramContext);
        }
        return personalDAO;
    }

    public PersonalDAO(Context paramContext) {
        initDBHelper(paramContext);
        initHttPostAux();
    }

    public PersonalE searchPersonal(String conditional) {

        Log.e(TAG, "start searchPersonal");

        personalE = new PersonalE();

        try
        {
            openDBHelper();

            SQL = "SELECT ps.dni, ps.id_cargo, cg.cargo, ps.nombre_completo, ps.estado_cambio, ps.estado_reemp, ps.r_dni, lc.nombreLocal FROM personal ps INNER JOIN local lc ON ps.cod_sede_operativa = lc.cod_sede_operativa AND ps.cod_local_sede = lc.cod_local_sede INNER JOIN cargo cg ON cg.id_cargo = ps.id_cargo WHERE " + conditional;
            //SQL = "SELECT ps.dni, ps.id_cargo, cg.cargo, ps.nombre_completo, ps.estado_cambio, ps.estado_reemp, ps.r_dni, lc.nombreLocal FROM personal ps INNER JOIN cargo cg ON cg.id_cargo = ps.id_cargo WHERE " + conditional;
            Log.e(TAG, "string sql : " + SQL);
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    LocalE localE = new LocalE();
                    localE.setNombreLocal(cursor.getString(cursor.getColumnIndex(LocalE.NOMBRE_LOCAL)));

                    CargoE cargoE = new CargoE();
                    cargoE.setCargo(cursor.getString(cursor.getColumnIndex(CargoE.CARGO)));

                    personalE.setDni(cursor.getString(cursor.getColumnIndex(PersonalE.DNI)));
                    personalE.setNombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.NOMBRE_COMPLETO)));
                    personalE.setLocalE(localE);
                    personalE.setCargoE(cargoE);
                    personalE.setEstadoReemplazo(cursor.getString(cursor.getColumnIndex(PersonalE.ESTADOREEMPLAZO)));
                    personalE.setId_cargo(cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO)));
                    personalE.setEstadoCambio(cursor.getString(cursor.getColumnIndex(PersonalE.ESTADOCAMBIO)));
                    personalE.setR_dni(cursor.getString(cursor.getColumnIndex(PersonalE.R_DNI)));
                    cursor.moveToNext();
                }
                personalE.setStatus(0);
            } else {
                SQL = "SELECT ps.dni, ps.id_cargo, cg.cargo, ps.nombre_completo, ps.estado_cambio, ps.estado_reemp, ps.r_dni FROM personal ps INNER JOIN cargo cg ON cg.id_cargo = ps.id_cargo WHERE " + conditional;
                Log.e(TAG, "string sql : " + SQL);
                cursor = dbHelper.getDatabase().rawQuery(SQL, null);
                if (cursor.moveToFirst()){
                    CargoE cargoE = new CargoE();
                    cargoE.setCargo(cursor.getString(cursor.getColumnIndex(CargoE.CARGO)));

                    personalE.setDni(cursor.getString(cursor.getColumnIndex(PersonalE.DNI)));
                    personalE.setNombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.NOMBRE_COMPLETO)));
                    personalE.setCargoE(cargoE);
                    personalE.setEstadoReemplazo(cursor.getString(cursor.getColumnIndex(PersonalE.ESTADOREEMPLAZO)));
                    personalE.setId_cargo(cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO)));
                    personalE.setEstadoCambio(cursor.getString(cursor.getColumnIndex(PersonalE.ESTADOCAMBIO)));
                    personalE.setR_dni(cursor.getString(cursor.getColumnIndex(PersonalE.R_DNI)));
                    personalE.setStatus(0);
                } else {
                    personalE.setStatus(1);// alerta. sin datos;
                    personalE.setEstadoReemplazo("0");
                }
            }
        Log.e(TAG, String.valueOf(personalE.getStatus()));
        Log.e(TAG, personalE.getEstadoReemplazo());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            personalE.setStatus(2); // error al acceder.
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchPersonal" );

        return personalE;
    }

    public Integer asistenciaPersonal(String nroDni) {

        Log.e( TAG, "start asistenciaPersonal" );

        try
        {
            openDBHelper();
            if (isEstadoRegistroPersonal(nroDni, PersonalE.ASISTENCIA)){
                valueInteger = 6 ;
            } else {
                SQL = "SELECT * FROM personal WHERE dni = '" + nroDni + "'";
                cursor = dbHelper.getDatabase().rawQuery(SQL, null);
                cursor.moveToFirst();
                String reeemplazo = cursor.getString(cursor.getColumnIndex(PersonalE.ESTADOREEMPLAZO));
                if(reeemplazo.equals("1") || reeemplazo.equals("2")) {
                    valueInteger = 16;
                } else {
                    contentValues =  new ContentValues();
                    contentValues.put( PersonalE.ASISTENCIA, "1" );
                    contentValues.put( PersonalE.HORA_INGRESO, ConstantsUtils.fecha_registro() );

                    SQL = "dni = '" + nroDni + "'";

                    valueInteger = dbHelper.getDatabase().updateWithOnConflict( "personal", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
                    Log.e( TAG, "asistencia personal : " + valueInteger.toString() );

                    valueInteger = 0;

                    dbHelper.setTransactionSuccessful();
                }

            }
        }
        catch (SQLiteDatabaseLockedException l){
            l.printStackTrace();
            valueInteger = 7;
            Log.e(TAG, "Error: " + l);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 3;// error al registra asistencia al local;
            Log.e(TAG, "Error: " + e);
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end asistenciaPersonal" );

        return valueInteger;
    }

    private boolean isEstadoRegistroPersonal(String nroDni, String column) {
        boolean isEstado = false;

        SQL = "SELECT " + column + " from personal where dni like '" + nroDni + "'";
        cursor = dbHelper.getDatabase().rawQuery( SQL, null );
        if ( cursor.moveToFirst() ) {
            while (!cursor.isAfterLast()) {
                int estado = 0;
                estado = cursor.getInt(cursor.getColumnIndex(column));
                if (estado != 0) {
                    isEstado = true;
                }
                Log.i(TAG, column + ": " + Integer.toString(estado));
                cursor.moveToNext();
            }
        }
        return isEstado;
    }

    public Integer searchPersonalCambio(String conditional) {
        Log.e(TAG, "start searchPersonalCambio");

        try
        {
            openDBHelper();

            SQL = "SELECT dni, estado_reemp FROM personal WHERE " + conditional;
            Log.e(TAG, "string sql : " + SQL);
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);
            if (cursor.moveToFirst()){
                String estadoReemplazo = cursor.getString(cursor.getColumnIndex(PersonalE.ESTADOREEMPLAZO));
               if (estadoReemplazo.equals("1") || estadoReemplazo.equals("2")){
                   valueInteger = 9;
               }else{
                  valueInteger = 8;
               }
            } else {
                valueInteger = 1;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 1;
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchPersonalCambio" );

        return valueInteger;
    }

    public Integer reemplazarPersonal(String dni, String dniCambio, String nombreCambio) {
        Log.e(TAG, "start reemplazarPersonal");
        try
        {
            openDBHelper();
            valueInteger = 10;
            contentValues =  new ContentValues();
            contentValues.put( PersonalE.ESTADOREEMPLAZO, "1" );
            contentValues.put( PersonalE.R_NOMBRE_COMPLETO, nombreCambio.toUpperCase() );
            contentValues.put(PersonalE.R_DNI, dniCambio.toUpperCase());
            contentValues.put(PersonalE.HORA_INGRESO, ConstantsUtils.fecha_registro());
            contentValues.put(PersonalE.ASISTENCIA, "1");

            SQL = "SELECT * FROM personal WHERE r_dni = '" + dniCambio + "'";
            Log.e(TAG, "string sql : " + SQL);
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);
            if (cursor.moveToFirst()){
                valueInteger = 12;
            } else {
                SQL = "SELECT * FROM personal WHERE dni = '" + dniCambio + "'";
                Log.e(TAG, "string sql : " + SQL);
                cursor = dbHelper.getDatabase().rawQuery(SQL, null);
                if (cursor.moveToFirst()){
                    String asistencia = cursor.getString(cursor.getColumnIndex(PersonalE.ASISTENCIA));
                    if (!asistencia.equals("")){
                        contentValues.put(PersonalE.HORA_SALIDA, cursor.getString(cursor.getColumnIndex(PersonalE.HORA_INGRESO)));
                    }
                    String reserva = cursor.getString(cursor.getColumnIndex(PersonalE.RESERVA));
                    Log.e(TAG, reserva);
                    if (reserva.equals("R")){
                        if (!cursor.getString(cursor.getColumnIndex(PersonalE.ASISTENCIA)).equals("0")){
                        contentValues.put(PersonalE.RESERVA, "1");
                        valueInteger = 10;
                        } else {
                            valueInteger = 17;
                        }
                    }
                    else {
                        valueInteger = 12;
                    }
                } else {
                    valueInteger = 10;
                }
            }
            if (valueInteger == 10) {
                SQL = "dni = '" + dni + "'";
                dbHelper.getDatabase().updateWithOnConflict("personal", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE);
                dbHelper.setTransactionSuccessful();
                Log.e(TAG, String.valueOf(valueInteger) + dniCambio + nombreCambio);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 2; // error al acceder.
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end reemplazarPersonal" );
        return valueInteger;
    }

    public PersonalE searchPersonalCambioCargo(String conditional) {
        Log.e(TAG, "start searchPersonalCambioCargo");

        personalE = new PersonalE();

        try
        {
            openDBHelper();

            SQL = "SELECT ps.dni, ps.r_dni, ps.id_cargo_cambio, cg.cargo, ps.r_nombre_completo, lc.nombreLocal FROM personal ps INNER JOIN local lc ON ps.cod_sede_operativa = lc.cod_sede_operativa AND ps.cod_local_sede = lc.cod_local_sede INNER JOIN cargo cg ON cg.id_cargo = ps.id_cargo_cambio WHERE " + conditional;
            Log.e(TAG, "string sql : " + SQL);
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);

            if (cursor.moveToFirst()) {


                    LocalE localE = new LocalE();
                    localE.setNombreLocal(cursor.getString(cursor.getColumnIndex(LocalE.NOMBRE_LOCAL)));

                    CargoE cargoE = new CargoE();
                    cargoE.setCargo(cursor.getString(cursor.getColumnIndex(CargoE.CARGO)));

                    personalE.setDni(cursor.getString(cursor.getColumnIndex(PersonalE.R_DNI)));
                    //personalE.setR_dni(cursor.getString(cursor.getColumnIndex(PersonalE.R_DNI)));
                    //personalE.setR_nombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.R_NOMBRE_COMPLETO)));
                    personalE.setNombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.R_NOMBRE_COMPLETO)));
                    //personalE.setId_cargo_cambio(cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO_CAMBIO)));
                    personalE.setId_cargo(cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO_CAMBIO)));
                    personalE.setLocalE(localE);
                    personalE.setCargoE(cargoE);

                    personalE.setStatus(0);
            } else {
                SQL = "SELECT ps.dni, ps.r_dni, ps.id_cargo_cambio, ps.id_cargo, cg.cargo, ps.r_nombre_completo, lc.nombreLocal FROM personal ps INNER JOIN local lc ON ps.cod_sede_operativa = lc.cod_sede_operativa AND ps.cod_local_sede = lc.cod_local_sede INNER JOIN cargo cg ON cg.id_cargo = ps.id_cargo WHERE " + conditional;
                Log.e(TAG, "string sql : " + SQL);
                cursor = dbHelper.getDatabase().rawQuery(SQL, null);

                if (cursor.moveToFirst()) {
                        LocalE localE = new LocalE();
                        localE.setNombreLocal(cursor.getString(cursor.getColumnIndex(LocalE.NOMBRE_LOCAL)));

                        CargoE cargoE = new CargoE();
                        cargoE.setCargo(cursor.getString(cursor.getColumnIndex(CargoE.CARGO)));

                        personalE.setDni(cursor.getString(cursor.getColumnIndex(PersonalE.R_DNI)));
                        //personalE.setR_dni(cursor.getString(cursor.getColumnIndex(PersonalE.R_DNI)));
                        //personalE.setR_nombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.R_NOMBRE_COMPLETO)));
                        personalE.setNombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.R_NOMBRE_COMPLETO)));
                        //personalE.setId_cargo_cambio(cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO_CAMBIO)));
                        personalE.setId_cargo(cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO)));
                        personalE.setLocalE(localE);
                        personalE.setCargoE(cargoE);
                        cursor.moveToNext();

                        personalE.setStatus(0);
                } else { personalE.setStatus(1); }// alerta. sin datos;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            personalE.setStatus(2); // error al acceder.
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchPersonalCambioCargo" );

        return personalE;
    }

    public int cambiarCargo(String dni, String cargo) {
        Log.e(TAG, "start cambiarCargo");

        try
        {
            openDBHelper();
            int idCargo;

            SQL = "SELECT * FROM personal WHERE dni = '" + dni + "' and reserva <> 'R'" ;
            Log.e(TAG, "string sql : " + SQL);
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);
            if (cursor.moveToFirst()){
                String asistencia = cursor.getString(cursor.getColumnIndex(PersonalE.ASISTENCIA));
                if(!asistencia.equals("0")){
                    SQL = "SELECT id_cargo from cargo where cargo = '" + cargo + "'";
                    Log.e(TAG, SQL);
                    Cursor c = dbHelper.getDatabase().rawQuery(SQL, null);
                    if (c.moveToFirst()){
                        idCargo = c.getInt(c.getColumnIndex(CargoE.IDCARGO));
                        contentValues = new ContentValues();
                        contentValues.put(PersonalE.ESTADOCAMBIO, "1");
                        contentValues.put(PersonalE.ID_CARGO_CAMBIO, idCargo);
                        SQL = "dni = '" + dni + "'";
                        valueInteger = dbHelper.getDatabase().updateWithOnConflict("personal", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE);
                        dbHelper.setTransactionSuccessful();
                        Log.e(TAG, "Rows = " + String.valueOf(valueInteger));
                        valueInteger = valueInteger > 0 ? 0 : 1;
                    }
                    c.close();
                } else {
                    valueInteger = 18;
                }

            }
            SQL = "SELECT * FROM personal WHERE r_dni = '" + dni + "'";
            Log.e(TAG, "string sql : " + SQL);
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);
            if (cursor.moveToFirst()){
                SQL = "SELECT id_cargo from cargo where cargo = '" + cargo + "'";
                Log.e(TAG, SQL);
                Cursor c = dbHelper.getDatabase().rawQuery(SQL, null);
                if (c.moveToFirst()){
                    idCargo = c.getInt(c.getColumnIndex(CargoE.IDCARGO));
                    contentValues = new ContentValues();
                    contentValues.put(PersonalE.ESTADOCAMBIO, "1");
                    contentValues.put(PersonalE.ID_CARGO_CAMBIO, idCargo);
                    SQL = "r_dni = '" + dni + "'";
                    valueInteger = dbHelper.getDatabase().updateWithOnConflict("personal", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE);
                    dbHelper.setTransactionSuccessful();
                    Log.e(TAG, "Rows = " + String.valueOf(valueInteger));
                    valueInteger = valueInteger > 0 ? 0 : 1;
                }
                c.close();

            }

        }
        catch (Exception e){
            e.printStackTrace();
            valueInteger = 2;
        }
        finally{
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end cambiarCargo" );

        return valueInteger;
    }

    public ArrayList<PersonalE> listadoPersonal() {
        Log.e( TAG, "start listadoPersonal" );

        personalEArrayList = new ArrayList<>();

        try {
            openDBHelper();
            SQL = "SELECT dni, nombre_completo, asistencia, r_dni, r_nombre_completo, id_cargo, id_cargo_cambio FROM personal";
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);
            if (cursor.moveToFirst()){
                while (!cursor.isAfterLast()){
                    personalE = new PersonalE();
                    personalE.setDni(cursor.getString(cursor.getColumnIndex(PersonalE.DNI)));
                    personalE.setNombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.NOMBRE_COMPLETO)));
                    String asistencia = cursor.getString(cursor.getColumnIndex(PersonalE.ASISTENCIA));
                    if (asistencia.equals("1") || asistencia.equals("2")){
                        personalE.setAsistencia("SI");
                    } else {
                        personalE.setAsistencia("NO");
                    }
                    personalE.setR_dni(cursor.getString(cursor.getColumnIndex(PersonalE.R_DNI)));
                    personalE.setR_nombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.R_NOMBRE_COMPLETO)));

                    int idCargoCambio = cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO_CAMBIO));
                    int idCargo = cursor.getInt(cursor.getColumnIndex(PersonalE.ID_CARGO));

                    if (idCargoCambio != 0){
                        String sql = "SELECT cargo_res FROM cargo WHERE id_cargo = " + idCargoCambio;
                        Cursor c = dbHelper.getDatabase().rawQuery(sql, null);
                        c.moveToFirst();
                        if (c.moveToFirst()){personalE.setCargo(c.getString(c.getColumnIndex(CargoE.CARGORES)));}
                        c.close();
                    } else {
                        String sql = "SELECT cargo_res FROM cargo WHERE id_cargo = " + idCargo;
                        Cursor c = dbHelper.getDatabase().rawQuery(sql, null);
                        if (c.moveToFirst()){personalE.setCargo(c.getString(c.getColumnIndex(CargoE.CARGORES)));}
                        c.close();
                    }

                    personalEArrayList.add(personalE);
                    cursor.moveToNext();
                }
            }
            Log.e(TAG, String.valueOf(personalEArrayList.size()));
        } catch ( Exception e )
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
        }
        finally
        {
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end listadoPersonal" );

        return personalEArrayList;

    }

    public String searchNombreReserva(String nroDni) {
        String nombreCompleto = "";
        try{
            openDBHelper();
            SQL = "SELECT  * FROM personal WHERE dni = '" + nroDni + "'";
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);
            if (cursor.moveToFirst()){
                nombreCompleto = cursor.getString(cursor.getColumnIndex(PersonalE.NOMBRE_COMPLETO));
            }
        } catch (Exception e){
            e.printStackTrace();
            Log.e( TAG, e.toString() );
        }finally
        {
            closeDBHelper();
            cursor.close();
        }

        return nombreCompleto;
    }

}
