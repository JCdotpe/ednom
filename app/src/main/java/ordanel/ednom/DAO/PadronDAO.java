package ordanel.ednom.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ordanel.ednom.BD.DBHelper;
import ordanel.ednom.Entity.PadronE;
import ordanel.ednom.Library.HttpPostAux;

/**
 * Created by OrdNael on 30/10/2014.
 */
public class PadronDAO {

    private static final String TAG = PadronDAO.class.getSimpleName();

    String IP_Server = "jc.pe";
    String URL_Connect = "http://" + IP_Server + "/portafolio/ednom/padron.php";
    /*String IP_Server = "172.16.100.45";
    String URL_Connect = "http://" + IP_Server + "/droidlogin/padron.php";*/

    ArrayList<PadronE> arrayList;

    Context context;

    public PadronDAO( Context context ) {
        this.context = context;
    }

    public ArrayList<PadronE> padronNube() {

        HttpPostAux httpPostAux = new HttpPostAux();

        JSONArray jsonArray = httpPostAux.getServerData( null, URL_Connect );

        if ( jsonArray != null && jsonArray.length() > 0 )
        {
            arrayList = new ArrayList<PadronE>();
            JSONObject jsonObject;

            Integer count = jsonArray.length();
            Log.e( TAG, count.toString() );

            try
            {
                for ( int i = 0; i < jsonArray.length(); i++ )
                {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    PadronE padronE = new PadronE();
                    padronE.setCodigo( jsonObject.getInt("Codigo") );
                    padronE.setSede( jsonObject.getString( "Sede" ) );
                    padronE.setNroLocal( jsonObject.getInt( "NroLocal") );
                    padronE.setLocalAplicacion( jsonObject.getString( "LocalAplicacion" ) );
                    padronE.setAula( jsonObject.getString( "Aula" ) );
                    padronE.setNumDoc( jsonObject.getString( "NumDoc" ) );
                    padronE.setApePaterno( jsonObject.getString( "ApePaterno" ) );
                    padronE.setApeMaterno( jsonObject.getString( "ApeMaterno" ) );
                    padronE.setNombres( jsonObject.getString( "Nombres" ) );
                    padronE.setStatus( jsonObject.getInt( "Status" ) );

                    arrayList.add( padronE );
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return arrayList;

        }
        else
        {
            return null;
        }

    }

    public Integer padronLocal( Integer idVersion ) {

        DBHelper dbHelper = DBHelper.getUtilDb( this.context );

        String success = "0";
        Cursor cursor = null;
        ContentValues contentValues;

        arrayList = this.padronNube();

        try
        {
            dbHelper.openDataBase();
            dbHelper.beginTransaction();

            String SQL = "SELECT COUNT(Id) AS numberRows FROM Padron";

            cursor = dbHelper.getDatabase().rawQuery( SQL, null );
            cursor.moveToFirst();

            Integer count = cursor.getInt(0);

            if ( count > 0 )
            {
                dbHelper.getDatabase().delete( "Padron", null, null );
                Log.e( TAG, "Se elimino Padron!" );

            }

            for ( int i = 0; i < arrayList.size(); i++ )
            {
                contentValues = new ContentValues();

                contentValues.put( "Codigo", arrayList.get(i).getCodigo() );
                contentValues.put( "Sede", arrayList.get(i).getSede() );
                contentValues.put( "NroLocal", arrayList.get(i).getNroLocal() );
                contentValues.put( "LocalAplicacion", arrayList.get(i).getLocalAplicacion() );
                contentValues.put( "Aula", arrayList.get(i).getAula() );
                contentValues.put( "NumDoc", arrayList.get(i).getNumDoc() );
                contentValues.put( "ApePaterno", arrayList.get(i).getApePaterno() );
                contentValues.put( "ApeMaterno", arrayList.get(i).getApeMaterno() );
                contentValues.put( "Nombres", arrayList.get(i).getNombres() );
                contentValues.put( "Status", arrayList.get(i).getStatus() );

                Long exito = dbHelper.getDatabase().insertOrThrow( "Padron", null, contentValues );
                success = String.valueOf(exito);
            }

            if ( !success.equals("0") && !success.equals("-1") )
            {
                dbHelper.getDatabase().delete( "Version", null, null );

                contentValues =  new ContentValues();
                contentValues.put( "idVersion" , idVersion );

                Long exito = dbHelper.getDatabase().insertOrThrow( "Version", null, contentValues );
                success = String.valueOf(exito);
            }

            dbHelper.setTransactionSuccessful();
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

        return Integer.valueOf(success);
    }

}