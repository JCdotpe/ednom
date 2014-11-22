package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.VersionE;
import ordanel.ednom.Library.ConstantsUtils;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 29/10/2014.
 */
public class VersionDAO {

    private static final String TAG = VersionDAO.class.getSimpleName();

    String URL_Connect = ConstantsUtils.BASE_URL + "version";// "version.php" "version"

    Integer error;
    Long valueLong;

    DBHelper dbHelper;
    Context context;
    ContentValues contentValues;

    JSONObject jsonObject;

    VersionE versionE;

    public VersionDAO (Context context) {
        this.context = context;
        Log.e( TAG, "start" );
    }

    public Integer currentVersion() {

        Log.e( TAG, "start currentVersion" );

        dbHelper = DBHelper.getUtilDb( this.context );
        Cursor cursor = null;

        versionE = new VersionE();

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT MAX(v_padron) AS currentVersion FROM version";

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
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();

            Log.e( TAG, "end currentVersion" );
        }

    }

    public VersionE checkVersion( Integer versionLocal ) {

        Integer versionNube;

        Log.e( TAG, "start checkVersion - version local: " + versionLocal.toString() );

        HttpPostAux httpPostAux = new HttpPostAux();
        JSONArray jsonArray = httpPostAux.getServerData( null, URL_Connect );

        versionE = new VersionE();

        if ( jsonArray != null )
        {
            if ( jsonArray.length() > 0 )
            {
                try
                {
                    jsonObject = (JSONObject) jsonArray.get(0);

                    versionNube = jsonObject.getInt( "v_padron" );

                    versionE.setVercod( jsonObject.getInt( "vercod" ) );
                    versionE.setV_padron( versionNube );
                    versionE.setV_sistem(jsonObject.getInt( "v_sistem" ) );
                    versionE.setFecha( jsonObject.getString( "fecha" ) );
                    versionE.setObserva( jsonObject.getString( "observa" ) );

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
        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            Integer rowsNumber = dbHelper.getDatabase().delete( "version", null, null );
            Log.e( TAG, "Se elimino version! : " + rowsNumber.toString() );

            contentValues =  new ContentValues();

            contentValues.put( "vercod", versionE.getVercod() );
            contentValues.put( "v_padron", versionE.getV_padron() );
            contentValues.put( "v_sistem", versionE.getV_sistem() );
            contentValues.put( "fecha", versionE.getFecha() );
            contentValues.put( "observa", versionE.getObserva() );

            valueLong = dbHelper.getDatabase().insertOrThrow( "version", null, contentValues );
            Log.e( TAG, "register : " + String.valueOf(valueLong) ); // -1 => error register
            error = 0;

            dbHelper.setTransactionSuccessful();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error registerVersion : " + e.toString() );
            error = 1; // error register
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
        }

        Log.e( TAG, "end registerVersion" );

        return error;
    }

}