package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import ordanel.ednom.Entity.VersionE;
import ordanel.ednom.Library.ConstantsUtils;

/**
 * Created by OrdNael on 29/10/2014.
 */
public class VersionDAO extends BaseDAO {

    private static final String TAG = VersionDAO.class.getSimpleName();
    private static VersionDAO versionDAO;

    VersionE versionE;

    public synchronized static VersionDAO getInstance( Context paramContext ) {

        if ( versionDAO == null )
        {
            versionDAO =new VersionDAO( paramContext );
        }

        return versionDAO;
    }

    private VersionDAO ( Context paramContext ) {

        initDBHelper( paramContext );
        initHttPostAux();

    }

    public Integer currentVersion() {

        Log.e( TAG, "start currentVersion" );

        versionE = new VersionE();

        try
        {
            openDBHelper();

            SQL = "SELECT MAX(v_padron) AS currentVersion FROM version";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                versionE.setV_padron( cursor.getInt( cursor.getColumnIndex( "currentVersion" ) ) );
            }

            return versionE.getV_padron();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error currentVersion : " + e.toString() );
            return null;
        }
        finally
        {
            closeDBHelper();
            cursor.close();

            Log.e( TAG, "end currentVersion" );
        }

    }

    public VersionE checkVersion( Integer versionLocal ) {

        Log.e( TAG, "start checkVersion - version local: " + versionLocal.toString() );

        jsonArray = httpPostAux.getServerData( null, ConstantsUtils.URL_VERSION );

        versionE = new VersionE();

        Integer versionNube;

        if ( jsonArray != null )
        {
            if ( jsonArray.length() > 0 )
            {
                try
                {
                    jsonObject = (JSONObject) jsonArray.get(0);

                    versionNube = jsonObject.getInt( VersionE.V_PADRON );

                    versionE.setVercod( jsonObject.getInt( VersionE.VERCOD ) );
                    versionE.setV_padron( versionNube );
                    versionE.setV_sistem(jsonObject.getInt( VersionE.V_SISTEM ) );
                    versionE.setFecha( jsonObject.getString( VersionE.FECHA ) );
                    versionE.setObserva( jsonObject.getString( VersionE.OBSERVA ) );

                    if ( !versionNube.equals(versionLocal) )
                    {
                        versionE.setStatus( 99 );
                    }
                    else
                    {
                        versionE.setStatus( 100 );
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    versionE.setStatus( 4 ); // error en checkVersion //
                    Log.e( TAG, "error checkVersion : " + e.toString() );
                }

            }
            else
            {
                versionE.setStatus( 3 ); // error en data //
            }
        }
        else
        {
            versionE.setStatus( 2 ); // error en HttpPostAux //
        }

        Log.e( TAG, "end checkVersion" );

        return versionE;

    }

    public Integer registerVersion( VersionE versionE ) {

        Log.e( TAG, "start registerVersion" );

        try
        {
            openDBHelper();

            Integer rowsNumber = dbHelper.getDatabase().delete( "version", null, null );
            Log.e( TAG, "Se elimino version! : " + rowsNumber.toString() );

            contentValues =  new ContentValues();

            contentValues.put( VersionE.VERCOD, versionE.getVercod() );
            contentValues.put( VersionE.V_PADRON, versionE.getV_padron() );
            contentValues.put( VersionE.V_SISTEM, versionE.getV_sistem() );
            contentValues.put( VersionE.FECHA, versionE.getFecha() );
            contentValues.put( VersionE.OBSERVA, versionE.getObserva() );

            valueLong = dbHelper.getDatabase().insertOrThrow( "version", null, contentValues );
            Log.e( TAG, "register : " + String.valueOf(valueLong) ); // -1 => error register

            if ( !valueLong.equals(0) && !valueLong.equals(-1) )
            {
                valueInteger = 0;
            }
            else
            {
                valueInteger = 1; // error register
            }

            dbHelper.setTransactionSuccessful();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error registerVersion : " + e.toString() );
            valueInteger = 1; // error register
        }
        finally
        {
            closeDBHelper();
        }

        Log.e( TAG, "end registerVersion" );

        return valueInteger;
    }

}