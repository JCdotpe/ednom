package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        initHttPostAux();
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

    public void getAllforSync() {

        Log.e( TAG, "start getAllforSync" );

        try
        {
            openDBHelper();

            String SQL = "SELECT nro_doc, estado, f_registro, estado_aula, f_aula, estado_ficha, f_ficha, estado_cartilla, f_cartilla, nro_aula_cambio FROM docentes";
            Log.e( TAG, "string sql : " + SQL );
            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            JSONArray jsonArray = new JSONArray();

            if ( cursor.moveToFirst() )
            {
                while ( !cursor.isAfterLast() )
                {

                    JSONObject jsonObjectTemp = new JSONObject();

                    jsonObjectTemp.put("nro_doc", cursor.getString(cursor.getColumnIndex("nro_doc")));
                    jsonObjectTemp.put("estado", cursor.getInt(cursor.getColumnIndex("estado")));
                    jsonObjectTemp.put("f_registro", cursor.getString(cursor.getColumnIndex("f_registro")));
                    jsonObjectTemp.put("estado_aula", cursor.getInt(cursor.getColumnIndex("estado_aula")));
                    jsonObjectTemp.put("f_aula", cursor.getString(cursor.getColumnIndex("f_aula")));
                    jsonObjectTemp.put("estado_ficha", cursor.getInt(cursor.getColumnIndex("estado_ficha")));
                    jsonObjectTemp.put("f_ficha", cursor.getString(cursor.getColumnIndex("f_ficha")));
                    jsonObjectTemp.put("estado_cartilla", cursor.getInt(cursor.getColumnIndex("estado_cartilla")));
                    jsonObjectTemp.put("f_cartilla", cursor.getString(cursor.getColumnIndex("f_cartilla")));
                    jsonObjectTemp.put("nro_aula_cambio", cursor.getInt(cursor.getColumnIndex("nro_aula_cambio")));

                    jsonArray.put(jsonObjectTemp);

                    cursor.moveToNext();

                }

            }

            /*jsonObjectGeneral.put( "data", jsonArray );*/

            String json = jsonArray.toString();

            ArrayList<NameValuePair> parametersPost = new ArrayList<NameValuePair>();
            parametersPost.add( new BasicNameValuePair( "data", json ) );

            Log.e( "Welcome", "json : " + jsonArray.toString() );
            String URL_Connect = ConstantsUtils.BASE_URL + "sync.php";

            JSONArray jsonArrayGet = httpPostAux.getServerData( parametersPost, URL_Connect );
            Log.e( "Welcome", "get json : " + jsonArrayGet.toString() );

            JSONObject jsonObject;

            if ( jsonArrayGet != null && jsonArrayGet.length() > 0 )
            {
                for ( int i = 0; i < jsonArrayGet.length(); i++ )
                {
                    jsonObject = (JSONObject) jsonArrayGet.get(i) ;

                    contentValues = new ContentValues();
                    contentValues.put( "estado", jsonObject.getInt( "estado" ) );
                    contentValues.put( "estado_aula", jsonObject.getInt( "estado_aula" ) );
                    contentValues.put( "estado_ficha", jsonObject.getInt( "estado_ficha" ) );
                    contentValues.put( "estado_cartilla", jsonObject.getInt( "estado_cartilla" ) );

                    String nro_doc = jsonObject.getString( "nro_doc" );
                    String Where = "nro_doc = '" + nro_doc + "'";
                    Log.e( TAG, "where : " + Where );
                    Integer valueInteger = dbHelper.getDatabase().updateWithOnConflict( "docentes", contentValues, Where, null, SQLiteDatabase.CONFLICT_IGNORE );
                    Log.e( TAG, "sync update : " + String.valueOf( valueInteger ) );

                }

                dbHelper.setTransactionSuccessful();
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