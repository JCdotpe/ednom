package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

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
        initHttPostAux();
    }

    public DocentesE searchPerson( String paramConditional ) {

        Log.e( TAG, "start searchPerson" );

        docentesE = new DocentesE();

        try
        {
            openDBHelper();

            SQL = "SELECT nro_doc, ape_pat, ape_mat, nombres, nro_aula, lc.nombreLocal FROM docentes dc INNER JOIN local lc ON dc.cod_sede_operativa = lc.cod_sede_operativa AND dc.cod_local_sede = lc.cod_local_sede WHERE " + paramConditional;
            Log.e( TAG, "string sql : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {

                    LocalE localE = new LocalE();
                    localE.setNombreLocal( cursor.getString( cursor.getColumnIndex( LocalE.NOMBRE_LOCAL) ) );

                    AulaLocalE aulaLocalE = new AulaLocalE();
                    aulaLocalE.setLocalE( localE );
                    aulaLocalE.setNro_aula( cursor.getInt( cursor.getColumnIndex( AulaLocalE.NRO_AULA ) ) );

                    docentesE.setNro_doc( cursor.getString( cursor.getColumnIndex( DocentesE.NRO_DOC ) ) );
                    docentesE.setApe_pat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_PAT ) ) );
                    docentesE.setApe_mat( cursor.getString( cursor.getColumnIndex( DocentesE.APE_MAT ) ) );
                    docentesE.setNombres( cursor.getString( cursor.getColumnIndex( DocentesE.NOMBRES ) ) );
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
            contentValues.put( DocentesE.ESTADO, 1 );
            contentValues.put( DocentesE.F_REGISTRO, ConstantsUtils.fecha_registro() );

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
            contentValues.put( DocentesE.ESTADO_AULA, 1 );
            contentValues.put( DocentesE.F_AULA, ConstantsUtils.fecha_registro() );

            if ( paramContingencia == 0 )
            {
                SQL = "nro_doc = '" + number_doc + "' and nro_aula = " + nro_aula + " and estado > 0";
            }
            else
            {
                SQL = "nro_doc = '" + number_doc + "' and estado > 0";
                contentValues.put( DocentesE.NRO_AULA_CAMBIO, nro_aula );
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

    public Integer inventarioFichaDocente( String paramCodFicha ) {

        Log.e( TAG, "start InventarioFichaDocente" );

        try
        {
            openDBHelper();

            contentValues = new ContentValues();
            contentValues.put( DocentesE.ESTADO_FICHA, 1 );
            contentValues.put( DocentesE.F_FICHA, ConstantsUtils.fecha_registro() );

            SQL = "cod_ficha = '" + paramCodFicha + "'";

            valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, SQL, null, SQLiteDatabase.CONFLICT_IGNORE );
            Log.e( TAG, "inventario ficha docente : " + valueInteger.toString() );

            if ( !valueInteger.equals(0) ) // el registro es correcto
            {
                valueInteger = 0;
            }
            else
            {
                valueInteger = 3; // error al registrar inventario de ficha
            }

            dbHelper.setTransactionSuccessful();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, e.toString() );
            valueInteger = 3;// error al registrar inventario de ficha;
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end InventarioFichaDocente" );

        return valueInteger;

    }

    public void getAllforSync() {

        Log.e( TAG, "start getAllforSync" );

        try
        {
            openDBHelper();

            SQL = "SELECT nro_doc, estado, f_registro, estado_aula, f_aula, estado_ficha, f_ficha, estado_cartilla, f_cartilla, nro_aula_cambio FROM docentes WHERE estado = 1 or estado_aula = 1 or estado_ficha = 1 or estado_cartilla = 1";
            Log.e( TAG, "string sql : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            jsonArray = new JSONArray();

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {

                    JSONObject jsonObjectTemp = new JSONObject();

                    jsonObjectTemp.put( DocentesE.NRO_DOC, cursor.getString( cursor.getColumnIndex( DocentesE.NRO_DOC ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO ) ) );
                    jsonObjectTemp.put( DocentesE.F_REGISTRO, cursor.getString( cursor.getColumnIndex( DocentesE.F_REGISTRO ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO_AULA, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO_AULA ) ) );
                    jsonObjectTemp.put( DocentesE.F_AULA, cursor.getString( cursor.getColumnIndex( DocentesE.F_AULA ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO_FICHA, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO_FICHA ) ) );
                    jsonObjectTemp.put( DocentesE.F_FICHA, cursor.getString( cursor.getColumnIndex( DocentesE.F_FICHA ) ) );
                    jsonObjectTemp.put( DocentesE.ESTADO_CARTILLA, cursor.getInt( cursor.getColumnIndex( DocentesE.ESTADO_CARTILLA ) ) );
                    jsonObjectTemp.put( DocentesE.F_CARTILLA, cursor.getString( cursor.getColumnIndex( DocentesE.F_CARTILLA ) ) );
                    jsonObjectTemp.put( DocentesE.NRO_AULA_CAMBIO, cursor.getInt( cursor.getColumnIndex( DocentesE.NRO_AULA_CAMBIO ) ) );

                    jsonArray.put(jsonObjectTemp);

                    cursor.moveToNext();

                }

                String json = jsonArray.toString();

                ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
                parametersPost.add( new BasicNameValuePair( "data", json ) );

                Log.e( "Welcome", "json : " + jsonArray.toString() );

                JSONArray jsonArrayGet = httpPostAux.getServerData( parametersPost, ConstantsUtils.URL_SYNC );
                Log.e( "Welcome", "get json : " + jsonArrayGet.toString() );

                if ( jsonArrayGet != null && jsonArrayGet.length() > 0 )
                {
                    for ( int i = 0; i < jsonArrayGet.length(); i++ )
                    {
                        jsonObject = (JSONObject) jsonArrayGet.get(i) ;

                        contentValues = new ContentValues();
                        contentValues.put( DocentesE.ESTADO, jsonObject.getInt( DocentesE.ESTADO ) );
                        contentValues.put( DocentesE.ESTADO_AULA, jsonObject.getInt( DocentesE.ESTADO_AULA ) );
                        contentValues.put( DocentesE.ESTADO_FICHA, jsonObject.getInt( DocentesE.ESTADO_FICHA ) );
                        contentValues.put( DocentesE.ESTADO_CARTILLA, jsonObject.getInt( DocentesE.ESTADO_CARTILLA ) );

                        String nro_doc = jsonObject.getString( "nro_doc" );
                        Where = "nro_doc = '" + nro_doc + "'";
                        Log.e( TAG, "where : " + Where );
                        valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                        Log.e( TAG, "sync update : " + String.valueOf( valueInteger ) );

                    }

                    dbHelper.setTransactionSuccessful();
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
            cursor.close();
        }

        Log.e( TAG, "end getAllforSync" );

    }

}