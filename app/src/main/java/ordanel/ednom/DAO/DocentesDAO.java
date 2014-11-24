package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by OrdNael on 05/11/2014.
 */
public class DocentesDAO {

    private static final String TAG = DocentesDAO.class.getSimpleName();
    String SQL;
    Integer valueInteger;

    DBHelper dbHelper;

    ConstantsUtils constantsUtils;

    Context context;
    Cursor cursor = null;
    ContentValues contentValues = null;

    public DocentesDAO(Context context) {
        this.context = context;
    }

    public DocentesE searchPerson( String paramConditional ) {

        Log.e( TAG, "start searchPerson" );

        dbHelper = DBHelper.getUtilDb( this.context );

        DocentesE docentesE = new DocentesE();

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula FROM docentes WHERE " + paramConditional;
            Log.e( TAG, "string sql : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {

                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( "nro_aula" ) ) );

                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( "nro_doc" ) ) );
                    docentesE.setApe_pat( cursor.getString( cursor.getColumnIndex( "ape_pat" ) ) );
                    docentesE.setApe_mat( cursor.getString( cursor.getColumnIndex( "ape_mat" ) ) );
                    docentesE.setNombres( cursor.getString( cursor.getColumnIndex( "nombres" ) ) );
                    docentesE.setAulaLocalE( aulaLocalE );

                    cursor.moveToNext();
                }

                docentesE.setStatus( 0 );
            }
            else
            {
                docentesE.setStatus( 1 );// alerta. sin datos;
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            docentesE.setStatus( 2 ); // error al acceder.
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();
        }

        Log.e( TAG, "end searchPerson" );

        return docentesE;

    }

    public Integer asistenciaLocal( String paramDNI ) {

        Log.e( TAG, "start asistenciaLocal" );

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            contentValues =  new ContentValues();
            contentValues.put( "estado", 1 );
            contentValues.put( "f_registro", constantsUtils.fecha_registro() );

            SQL = "nro_doc = '" + paramDNI + "'";

            valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
            Log.e( TAG, "asistencia local : " + valueInteger.toString() );

            valueInteger = 0;

            dbHelper.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 3;// error al registra asistencia al local;
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
        }

        Log.e( TAG, "end asistenciaLocal" );

        return valueInteger;
    }

    public Integer asistenciaAula( String number_doc, Integer nro_aula, Integer paramContingencia ) {

        Log.e( TAG, "start asistenciaAula" );

        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            contentValues =  new ContentValues();
            contentValues.put( "estado_aula", 1 );
            contentValues.put( "f_aula", constantsUtils.fecha_registro() );

            if ( paramContingencia == 0 )
            {
                SQL = "nro_doc = '" + number_doc + "' and nro_aula = " + nro_aula + " and estado > 0";
            }
            else
            {
                SQL = "nro_doc = '" + number_doc + "' and estado > 0";
                contentValues.put( "nro_aula_cambio", nro_aula );
            }

            valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
            Log.e( TAG, "asistencia aula : " + valueInteger.toString() );

            if ( !valueInteger.equals(0) ) // el registro es correcto
            {
                valueInteger = 0;
            }
            else
            {
                valueInteger = 5; // el docente no fue registrado al ingreso del local
            }

            dbHelper.setTransactionSuccessful();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            valueInteger = 4;// error al registrar asistencia al aula;
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
        }

        Log.e( TAG, "end asistenciaAula" );

        return valueInteger;
    }

}