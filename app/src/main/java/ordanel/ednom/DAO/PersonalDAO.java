package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.util.Log;

import java.util.ArrayList;

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

            SQL = "SELECT ps.dni, ps.id_cargo, cg.cargo, ps.nombre_completo, lc.nombreLocal FROM personal ps " +
                    "INNER JOIN local lc ON ps.cod_sede_operativa = lc.cod_sede_operativa AND ps.cod_local_sede = lc.cod_local_sede " +
                    "INNER JOIN cargo cg ON cg.id_cargo = ps.id_cargo WHERE " + conditional;
            Log.e(TAG, "string sql : " + SQL);
            cursor = dbHelper.getDatabase().rawQuery(SQL, null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {

                    LocalE localE = new LocalE();
                    localE.setNombreLocal(cursor.getString(cursor.getColumnIndex(LocalE.NOMBRE_LOCAL)));

                    personalE.setDni(cursor.getString(cursor.getColumnIndex(PersonalE.DNI)));
                    personalE.setNombre_completo(cursor.getString(cursor.getColumnIndex(PersonalE.NOMBRE_COMPLETO)));
                    cursor.moveToNext();
                }

                personalE.setStatus(0);
            } else {
                personalE.setStatus(1);// alerta. sin datos;
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

        Log.e( TAG, "end searchPersonal" );

        return personalE;
    }

    public int asistenciaPersonal(String nroDni) {

        Log.e( TAG, "start asistenciaPersonal" );

        try
        {
            openDBHelper();
            if (isEstadoRegistroPersonal(nroDni, PersonalE.ASISTENCIA)){
                valueInteger = 6 ;
            } else {
                contentValues =  new ContentValues();
                contentValues.put( PersonalE.ASISTENCIA, 1 );
                contentValues.put( PersonalE.HORA_INGRESO, ConstantsUtils.fecha_registro() );

                SQL = "dni = '" + nroDni + "'";

                valueInteger = dbHelper.getDatabase().updateWithOnConflict( "personal", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
                Log.e( TAG, "asistencia personal : " + valueInteger.toString() );

                valueInteger = 0;

                dbHelper.setTransactionSuccessful(); }
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

        SQL = "SELECT " + column + " from docentes where dni like '" + nroDni + "'";
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
}
