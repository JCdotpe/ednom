package ordanel.ednom.DAO;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.VersionE;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 29/10/2014.
 */
public class VersionDAO {

    private static final String TAG = VersionDAO.class.getSimpleName();

    String IP_Server = "jc.pe";
    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/version.php";
    /*String IP_Server = "172.16.100.45";
    String URL_Connect = "http://" + IP_Server + "/droidlogin/version.php";*/

    Context context;

    public VersionDAO (Context context) {
        this.context = context;
        Log.e( TAG, "start" );
    }

    public Integer currentVersion() {

        DBHelper dbHelper = DBHelper.getUtilDb( this.context );

        Cursor cursor = null;

        VersionE versionE = new VersionE();

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT MAX(idVersion) AS currentVersion FROM Version";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );

            if ( cursor.moveToFirst() )
            {
                /*while ( !cursor.isAfterLast() )
                {
                    versionE.setIdVersion( cursor.getInt( cursor.getColumnIndex( "currentVersion" ) ) );

                    cursor.moveToNext();
                }*/

                versionE.setIdVersion( cursor.getInt( cursor.getColumnIndex( "currentVersion" ) ) );
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbHelper.endTransaction();
            dbHelper.close();
            cursor.close();
        }

        return versionE.getIdVersion();
    }

    public Integer checkVersion() {

        Integer versionNube, versionLocal;
        Integer statusVersion = 0;

        versionLocal = this.currentVersion();
        Log.e( TAG, "version local : " + versionLocal.toString() );

        HttpPostAux httpPostAux = new HttpPostAux();

        JSONArray jsonArray = httpPostAux.getServerData( null, URL_Connect );

        if ( jsonArray != null && jsonArray.length() > 0 )
        {
            try
            {
                JSONObject jsonObject;

                jsonObject = (JSONObject) jsonArray.get(0);

                versionNube = jsonObject.getInt( "idVersion" );

                if ( !versionNube.equals(versionLocal) )
                {
                    statusVersion = versionNube;
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return statusVersion;

    }

}