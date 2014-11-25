package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ordanel.ednom.Entity.AulaLocalE;
import ordanel.ednom.Entity.DocentesE;
import ordanel.ednom.Entity.LocalE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by OrdNael on 05/11/2014.
 */
public class DocentesDAO extends BaseDAO {

    private static final String TAG = DocentesDAO.class.getSimpleName();
    private static DocentesDAO docentesDAO;

    String SQL;
    Integer valueInteger;

    ConstantsUtils constantsUtils;

    Cursor cursor = null;
    ContentValues contentValues = null;

    DocentesE docentesE;

    public static DocentesDAO getInstance( Context paramContext ) {

        if ( docentesDAO == null )
        {
            docentesDAO = new DocentesDAO( paramContext );
        }

        return docentesDAO;
    }

    private DocentesDAO( Context paramContext ) {
        initDBHelper( paramContext );
    }

    public DocentesE searchPerson( String paramConditional ) {

        Log.e( TAG, "start searchPerson" );

        docentesE = new DocentesE();

        try
        {
            openDBHelper();

            String SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula, lc.nombreLocal FROM docentes dc INNER JOIN local lc ON dc.cod_sede_operativa = lc.cod_sede_operativa AND dc.cod_local_sede = lc.cod_local_sede WHERE " + paramConditional;
            Log.e( TAG, "string sql : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {

                    LocalE localE = new LocalE();
                    localE.setNombreLocal( cursor.getString( cursor.getColumnIndex( "nombreLocal" ) ) );

                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setLocalE( localE );
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
            closeDBHelper();
            cursor.close();
        }

        Log.e( TAG, "end searchPerson" );

        return docentesE;

    }

    public Integer asistenciaLocal( String paramDNI ) {

        Log.e( TAG, "start asistenciaLocal" );

        try
        {
            openDBHelper();

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
            closeDBHelper();
        }

        Log.e( TAG, "end asistenciaLocal" );

        return valueInteger;
    }

    public Integer asistenciaAula( String number_doc, Integer nro_aula, Integer paramContingencia ) {

        Log.e( TAG, "start asistenciaAula" );

        try
        {
            openDBHelper();

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
            closeDBHelper();
        }

        Log.e( TAG, "end asistenciaAula" );

        return valueInteger;
    }

    public ArrayList<DocentesE> getAllforSync() {

        Log.e( TAG, "start getAllforSync" );

        ArrayList<DocentesE> docentesEArrayList = new ArrayList<DocentesE>();

        try
        {
            openDBHelper();

            String SQL = "SELECT nro_doc, estado, f_registro, estado_aula, f_aula, estado_ficha, f_ficha, estado_cartilla, f_cartilla, nro_aula_cambio FROM docentes";
            Log.e( TAG, "string sql : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {
                    docentesE = new DocentesE();
                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( "nro_doc" ) ) );
                    docentesE.setEstado( cursor.getInt( cursor.getColumnIndex( "estado" ) ) );
                    docentesE.setF_registro( cursor.getString( cursor.getColumnIndex( "f_registro" ) ) );
                    docentesE.setEstado_aula( cursor.getInt( cursor.getColumnIndex( "estado_aula" ) ) );
                    docentesE.setF_aula( cursor.getString( cursor.getColumnIndex( "f_aula" ) ) );
                    docentesE.setEstado_ficha( cursor.getInt( cursor.getColumnIndex( "estado_ficha" ) ) );
                    docentesE.setF_ficha( cursor.getString( cursor.getColumnIndex( "f_ficha" ) ) );
                    docentesE.setEstado_cartilla( cursor.getInt( cursor.getColumnIndex( "estado_cartilla" ) ) );
                    docentesE.setF_caritlla( cursor.getString( cursor.getColumnIndex( "f_cartilla" ) ) );
                    docentesE.setNro_aula_cambio( cursor.getInt( cursor.getColumnIndex( "nro_aula_cambio" ) ) );

                    docentesEArrayList.add( docentesE );

                    cursor.moveToNext();

                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "getAllforSync : " + e.toString() );
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end getAllforSync" );

        return docentesEArrayList;
    }

}