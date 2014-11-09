package ordanel.ednom.DAO;

import android.content.ContentValues;
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

    Integer error = 0;

    String IP_Server = "jc.pe";
    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/version.php";

    DBHelper dbHelper;
    Context context;

    public VersionDAO (Context context) {
        this.context = context;
        Log.e( TAG, "start" );
    }

    public Integer currentVersion() {

        Log.e( TAG, "start currentVersion" );

        dbHelper = DBHelper.getUtilDb( this.context );
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

            return versionE.getIdVersion();
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

    public Integer checkVersion() {

        Integer versionNube, versionLocal;
        Integer statusVersion;

        Log.e( TAG, "start checkVersion" );

        versionLocal = this.currentVersion();

        if ( versionLocal != null )
        {
            Log.e( TAG, "version local : " + versionLocal.toString() );

            HttpPostAux httpPostAux = new HttpPostAux();

            JSONArray jsonArray = httpPostAux.getServerData( null, URL_Connect );

            if ( jsonArray != null )
            {
                if ( jsonArray.length() > 0 )
                {
                    try
                    {
                        JSONObject jsonObject;

                        jsonObject = (JSONObject) jsonArray.get(0);

                        versionNube = jsonObject.getInt( "idVersion" );

                        if ( !versionNube.equals(versionLocal) )
                        {
                            if ( this.registerVersion( versionNube ) == 0 )
                            {
                                statusVersion = 99;
                            }
                            else
                            {
                                statusVersion = 5; // error en registerVersion //
                            }
                        }
                        else
                        {
                            statusVersion = 100;
                        }

                        return statusVersion;

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        error = 4; // error en checkVersion //
                        Log.e( TAG, "error checkVersion : " + e.toString() );
                    }
                    finally
                    {
                        Log.e( TAG, "end checkVersion" );
                    }
                }
                else
                {
                    error = 3; // error en data //
                }
            }
            else
            {
                error = 2; // error en HttpPostAux //
            }
        }
        else
        {
            error = 1; // error en currentVersion //
        }

        Log.e( TAG, "end checkVersion" );

        return error;

    }

    public Integer registerVersion( Integer idVersion ) {

        Log.e( TAG, "start registerVersion" );
        dbHelper = DBHelper.getUtilDb( this.context );

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String success = "0";

            Integer rowsNumber = dbHelper.getDatabase().delete( "Version", null, null );
            Log.e( TAG, "Se elimino Version! : " + rowsNumber.toString() );

            ContentValues contentValues =  new ContentValues();

            contentValues.put( "Id", 1 );
            contentValues.put( "idVersion", idVersion );
            contentValues.put( "Descripcion", "Padron v" + idVersion );

            Long exito = dbHelper.getDatabase().insertOrThrow( "Version", null, contentValues );
            success = String.valueOf(exito);

            if ( success.equals("0") && success.equals("-1") )
            {
                error = 1;
            }

            dbHelper.setTransactionSuccessful();

        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e( TAG, "error registerVersion : " + e.toString() );
            error = 1;
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